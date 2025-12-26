package org.megalodonte.utils;

import javafx.scene.paint.Color;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Commons {
    public static String FX_FontSize = "-fx-font-size";

    /**
     * Usado em Text
     */
    public static String FX_FILL = "-fx-fill";

    public static String UpdateEspecificStyle(
            String currentStyle,
            String targetField,
            String value) {

        // Cria a string de estilo com o valor a ser atualizado
        String newStyle = targetField + ": " + value + ";";

        // Verifica se o estilo já contém o campo de destino
        if (currentStyle.contains(targetField)) {
            // Substitui a parte do estilo correspondente ao targetField com o novo valor
            currentStyle = currentStyle.replaceAll(
                    "(?i)" + targetField + ":\\s*[^;]+;", // Captura o campo de destino e o valor atual, ignorando
                    // espaços extras
                    newStyle); // Substitui com o novo valor
        } else {
            // Se não houver, adiciona o novo estilo no final
            if (!currentStyle.endsWith(" ")) { // Evita duplicação de espaços
                currentStyle += " ";
            }
            currentStyle += newStyle; // Adiciona o novo estilo ao final
        }

        // Para verificar o estilo final (opcional, apenas para depuração)
        System.out.println("Updated Style: " + currentStyle);

        return currentStyle;
    }

    public static String getValueOfSpecificField(
            String currentStyle,
            String targetField) {

        // Verifica se o campo está presente
        if (currentStyle.contains(targetField)) {
            // Expressão regular para capturar o valor do campo, tratando espaços extras e
            // valores de cor
            String regex = targetField + ":\\s*([^;]+);"; // \\s* permite espaços extras
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(currentStyle);

            // Se encontrar uma correspondência, retorna o valor
            if (matcher.find()) {
                return matcher.group(1); // grupo 1 contém o valor após ":"
            }
        }

        // Se não encontrar o campo, retorna uma string vazia
        return "";
    }

    public static String ColortoHex(Color color) {
        return String.format("#%02x%02x%02x",
                (int) (color.getRed() * 255),
                (int) (color.getGreen() * 255),
                (int) (color.getBlue() * 255));
    }


    static void main() {
        //getVariableNamesInDataTable().forEach(IO::println);
        // getValuesFromVariablename("colors").forEach(IO::println);
    }

}
