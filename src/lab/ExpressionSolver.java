package lab;

import java.util.*;

/** 
 * Класс, вычисляющий знаение выражения.
 * @autor kruch(HallAzzy)
*/
public class ExpressionSolver {
	/** поле для хранения выражения */
    private String expression;
    static final Map<String, Integer> OPERATORS = Map.of(
        "+", 1,
        "-", 1,
        "*", 2,
        "/", 2
    );
    /** словарь для хранения переменных */
    static final Map<String, Double> VARIABLES = new HashMap<>();
    /** геттер выражения */
    public String getExpression() {
        return expression;
    }
    /** сеттер выражения */
    public void setExpression(String expression) {
        this.expression = expression;
    }

    /** метод проверки на число */
    static boolean isNumber(String token) {
        try {
            Double.parseDouble(token);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /** метод проверки на переменную */
    static boolean isVariable(String token) {
        for (char ch : token.toCharArray()) {
            if (!Character.isLetter(ch)) {
                return false;
            }
        }
        return true;
    }
    
    /** метод разбиения на составляющие */
    private List<String> tokenize(String expression) {
        List<String> tokens = new ArrayList<>();
        StringBuilder currentToken = new StringBuilder();
        
        for (char ch : expression.toCharArray()) {
            if (Character.isWhitespace(ch)) {
                continue;
            } else if (Character.isDigit(ch) || ch == '.') {
                currentToken.append(ch);
            } else if (Character.isLetter(ch)) {
                currentToken.append(ch);
            } else if (OPERATORS.containsKey(String.valueOf(ch)) || ch == '(' || ch == ')') {
                if (currentToken.length() > 0) {
                    tokens.add(currentToken.toString());
                    currentToken.setLength(0);
                }
                tokens.add(String.valueOf(ch));
            } else {
                throw new IllegalArgumentException("Недопустимый символ: " + ch);
            }
        }
        
        if (currentToken.length() > 0) {
            tokens.add(currentToken.toString());
        }
        
        return tokens;
    }

    /** метод преобразования в обратную польскую запись */
    private List<String> polishExpression(List<String> tokens) {
        List<String> output = new ArrayList<>();
        Stack<String> operators = new Stack<>();

        for (String token : tokens) {
            if (isNumber(token) || isVariable(token)) {
                output.add(token);
            } else if (OPERATORS.containsKey(token)) {
                while (!operators.isEmpty() && OPERATORS.containsKey(operators.peek()) &&
                       OPERATORS.get(token) <= OPERATORS.get(operators.peek())) {
                    output.add(operators.pop());
                }
                operators.push(token);
            } else if (token.equals("(")) {
                operators.push(token);
            } else if (token.equals(")")) {
                while (!operators.isEmpty() && !operators.peek().equals("(")) {
                    output.add(operators.pop());
                }
                if (operators.isEmpty() || !operators.pop().equals("(")) {
                    throw new IllegalArgumentException("Несоответствие скобок");
                }
            } else {
                throw new IllegalArgumentException("Недопустимый токен: " + token);
            }
        }

        while (!operators.isEmpty()) {
            String op = operators.pop();
            if (op.equals("(") || op.equals(")")) {
                throw new IllegalArgumentException("Несоответствие скобок");
            }
            output.add(op);
        }

        return output;
    }

    /** метод вычисления значения польской записи */
    private double evaluatepPolishExpression(List<String> rpn) {
        Stack<Double> stack = new Stack<>();
        Scanner scanner = new Scanner(System.in);

        for (String token : rpn) {
            if (isNumber(token)) {
                stack.push(Double.parseDouble(token));
            } else if (isVariable(token)) {
                if (!VARIABLES.containsKey(token)) {
                    System.out.print("Введите значение для " + token + ": ");
                    VARIABLES.put(token, scanner.nextDouble());
                }
                stack.push(VARIABLES.get(token));
            } else if (OPERATORS.containsKey(token)) {
                double b = stack.pop();
                double a = stack.pop();
                switch (token) {
                    case "+" -> stack.push(a + b);
                    case "-" -> stack.push(a - b);
                    case "*" -> stack.push(a * b);
                    case "/" -> stack.push(a / b);
                }
            } else {
                throw new IllegalArgumentException("Неизвестный оператор: " + token);
            }
        }

        return stack.pop();
    }

    /** основной метод для вычисления выражения */
    public double solve() {
        List<String> tokens = tokenize(expression);
        List<String> rpn = polishExpression(tokens);
        return evaluatepPolishExpression(rpn);
    }
}
