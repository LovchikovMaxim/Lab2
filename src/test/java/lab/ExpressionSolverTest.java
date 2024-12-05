package lab;

import org.junit.jupiter.api.Test;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ExpressionSolverTest {

    @Test
    void testSolve_WithExpression() {
        ExpressionSolver solver = new ExpressionSolver();
        solver.setExpression("5 + 5 * (2 - 8)");

        double result = solver.solve();
        assertEquals(-25.0, result);
    }

    @Test
    void testSolve_WithVariables() {
        ExpressionSolver solver = new ExpressionSolver();
        ExpressionSolver.VARIABLES.put("a", 1.0);
        ExpressionSolver.VARIABLES.put("b", 2.0);
        solver.setExpression("a + b * 3");

        double result = solver.solve();
        assertEquals(7.0, result);
    }

    @Test
    void testInvalidExpression() {
        ExpressionSolver solver = new ExpressionSolver();
        solver.setExpression("3 + * 5");

        Exception exception = assertThrows(IllegalArgumentException.class, solver::solve);
        assertEquals("Недопустимый токен: *", exception.getMessage());
    }

    @Test
    void testMismatchedParentheses() {
        ExpressionSolver solver = new ExpressionSolver();
        solver.setExpression("(3 + 5");

        Exception exception = assertThrows(IllegalArgumentException.class, solver::solve);
        assertEquals("Несоответствие скобок", exception.getMessage());
    }
}