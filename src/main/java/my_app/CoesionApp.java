package my_app;

import javafx.scene.layout.Region;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.annotation.ElementType;


@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface CoesionApp {
    String[] stylesheets() default {};

    // 🛑 NOVO CAMPO: Define a classe de entrada (Root View) para a UI principal
    Class<? extends Region> mainViewClass();
}