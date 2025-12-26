package my_app;

import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.scene.layout.Region;
import javafx.stage.Window;
import javafx.application.Platform;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

public class UIReloaderImpl1 implements Reloader {

    private static final String INIT_METHOD_NAME = "initView";

    @Override
    public void reload(Object context) {
        if (context instanceof Stage mainStage) {

            Platform.runLater(() -> {

                try {
                    // Usar o System ClassLoader para carregar a classe App (está na exclusão)
                    Class<?> originalAppClass = Class.forName("my_app.App");
                    CoesionApp globalAnnotation = originalAppClass.getAnnotation(CoesionApp.class);

                    if (globalAnnotation == null) {
                        System.err.println("Annotation @CoesionApp not found on App class.");
                        return;
                    }

                    List<String> globalStyles = Arrays.asList(globalAnnotation.stylesheets());

                    // 🛑 1. Obtém o nome da Main View a partir da anotação (via System CL)
                    Class<? extends Region> mainViewClassOld = globalAnnotation.mainViewClass();
                    String mainViewClassName = mainViewClassOld.getName();

                    // 🛑 2. Carrega a MainView usando o ClassLoader ATUAL (HotReloadClassLoader),
                    // garantindo que obtemos a versão mais recente da classe.
                    ClassLoader currentClassLoader = this.getClass().getClassLoader();
                    Class<? extends Region> actualMainViewClass = (Class<? extends Region>) currentClassLoader
                            .loadClass(mainViewClassName);

                    // 🛑 3. Instancia a nova View
                    Region newMainView = createNewViewInstance(actualMainViewClass);

                    Class<?> viewClass = newMainView.getClass();
                    Method initMethod = null;
                    try {
                        initMethod = viewClass.getDeclaredMethod(INIT_METHOD_NAME);
                        initMethod.setAccessible(true);
                    } catch (NoSuchMethodException e) {
                        System.out.println("[UIReloader] Optional initView() method not found in " + viewClass.getSimpleName() + ".");
                    }

                    // B) Recarrega Views e Estilos em Windows Secundárias (@ReloadableWindow) - Faz Injeção
                    for (Field field : viewClass.getDeclaredFields()) {
                        if (field.isAnnotationPresent(ReloadableWindow.class) && field.getType().equals(Stage.class)) {

                            field.setAccessible(true);
                            Stage stageToModify;
                            ReloadableWindow windowAnnotation = field.getAnnotation(ReloadableWindow.class);
                            String windowTitle = windowAnnotation.title();

                            // 🛑 1. Tenta encontrar a Stage Ativa (para recarga)
                            Stage activeSecondaryStage = findActiveStageByTitle(windowTitle);

                            if (activeSecondaryStage != null) {
                                stageToModify = activeSecondaryStage;
                                System.out.println("[UIReloader] Secondary Stage found active: " + windowTitle);
                            } else {
                                Stage newlyCreatedStage = new Stage();
                                stageToModify = newlyCreatedStage;
                                System.out.println("[UIReloader] Stage created and initialized: " + field.getName());
                            }

                            // Injeta a Stage na nova MainView
                            field.set(newMainView, stageToModify);

                            // 🛑 2. Configura a Stage
                            if (!windowTitle.equals(stageToModify.getTitle())) {
                                stageToModify.setTitle(windowTitle);
                            }

                            Scene secondaryScene = stageToModify.getScene();

                            // 🛑 3. Configuração da Scene

                            // Carrega a classe de conteúdo secundária usando o HRC para obter a versão mais recente
                            Class<? extends Region> contentClassOld = windowAnnotation.contentClass();
                            Class<? extends Region> actualContentClass = (Class<? extends Region>) currentClassLoader
                                    .loadClass(contentClassOld.getName());
                            Region newContent = createNewViewInstance(actualContentClass);

                            if (secondaryScene == null) {
                                // Inicialização completa (Scene não existe)
                                secondaryScene = new Scene(newContent, windowAnnotation.width(), windowAnnotation.height());
                                stageToModify.setScene(secondaryScene);
                                System.out.println("[UIReloader] New Scene initialized for secondary Stage.");
                            } else {
                                // Recarga de Conteúdo (Scene já existe)
                                secondaryScene.setRoot(newContent);
                                System.out.println("[UIReloader] Secondary Scene content reloaded.");
                            }

                            // 🛑 4. Aplica Tamanho e Estilos
                            Window window = secondaryScene.getWindow();
                            if (window != null) {
                                window.setWidth(windowAnnotation.width());
                                window.setHeight(windowAnnotation.height());
                            }

                            List<String> localStyles = Arrays.asList(windowAnnotation.stylesheets());
                            secondaryScene.getStylesheets().clear();
                            applyStylesToScene(secondaryScene, globalStyles, originalAppClass);
                            applyStylesToScene(secondaryScene, localStyles, originalAppClass);
                        }
                    }

                    // 🛑 Chamada do método de ciclo de vida APÓS a injeção de todos os campos
                    if (initMethod != null) {
                        try {
                            initMethod.invoke(newMainView);
                            System.out.println("[UIReloader] Executed initView() on new view instance.");
                        } catch (InvocationTargetException | IllegalAccessException e) {
                            System.err.println("Error executing initView() method.");
                            e.printStackTrace();
                        }
                    }

                    // 2. Recria a estrutura da UI principal (aplica a recarga de código)
                    // USANDO A LÓGICA QUE VOCÊ CONFIRMOU QUE FUNCIONA: substitui o filho do App.ROOT
                    Scene mainScene = mainStage.getScene();
                    if (mainScene != null) {
                        StackPane root = (StackPane) mainScene.getRoot();
                        root.getChildren().setAll(newMainView);

                        // A) Aplica estilos na SCENE PRINCIPAL
                        mainScene.getStylesheets().clear(); // Limpa antes de aplicar
                        applyStylesToScene(mainScene, globalStyles, originalAppClass);
                    }


                    System.out.println("[UIReloader] UI updated and CSS re-applied to all scenes.");

                } catch (ClassNotFoundException e) {
                    System.err.println("Error during reflection or class loading: Class not found, likely due to ClassLoader mismatch.");
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    System.err.println("Reflection error during Stage injection/access.");
                    e.printStackTrace();
                }
            });
        }
    }

    /**
     * Tenta encontrar uma Stage aberta pelo seu título.
     */
    private Stage findActiveStageByTitle(String title) {
        if (title == null || title.isEmpty()) return null;
        for (Window window : Window.getWindows()) {
            if (window instanceof Stage stage && title.equals(stage.getTitle())) {
                if (stage.isShowing()) {
                    return stage;
                }
            }
        }
        return null;
    }

    /**
     * Instancia uma nova Region.
     */
    private Region createNewViewInstance(Class<? extends Region> viewClass) {
        try {
            return viewClass.getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException e) {
            System.err.println("[UIReloader] Could not instantiate content view class: " + viewClass.getName());
            System.err.println("Check if the view class has a public no-argument constructor.");
            e.printStackTrace();
            return new StackPane();
        }
    }

    private void applyStylesToScene(Scene scene, List<String> stylesPaths, Class<?> contextClassForResources) {
        if (scene == null || stylesPaths.isEmpty()) return;

        for (String stylePath : stylesPaths) {
            try {
                URL resource = contextClassForResources.getResource(stylePath);
                if (resource != null) {
                    String cssUrl = resource.toExternalForm();
                    if (!scene.getStylesheets().contains(cssUrl)) {
                        scene.getStylesheets().add(cssUrl);
                        System.out.println("[UIReloader] CSS reloaded: " + stylePath);
                    }
                } else {
                    System.err.println("[UIReloader] Resource not found: " + stylePath);
                }
            } catch (Exception e) {
                System.err.println("[UIReloader] Error loading resource: " + stylePath + " using class " + contextClassForResources.getName());
                e.printStackTrace();
            }
        }
    }
}