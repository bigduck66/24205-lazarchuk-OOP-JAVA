package calculator;

import calculator.context.ExecutionContext;
import org.junit.jupiter.api.*;

import java.io.*;
import java.nio.file.*;

import static org.junit.jupiter.api.Assertions.*;

public class CalculatorTest {
    private Calculator calculator;
    private ExecutionContext context;
    
    @BeforeEach
    public void setUp() {
        calculator = new Calculator();
        context = calculator.getContext();
    }
    
    @Test
    public void testPushCommand() {
        calculator.executeCommand("PUSH 5");
        assertEquals(5.0, context.peek(), 0.001);
    }
    
    @Test
    public void testPushParameter() {
        calculator.executeCommand("DEFINE a 10");
        calculator.executeCommand("PUSH a");
        assertEquals(10.0, context.peek(), 0.001);
    }
    
    @Test
    public void testPopCommand() {
        calculator.executeCommand("PUSH 10");
        calculator.executeCommand("PUSH 20");
        calculator.executeCommand("POP");
        assertEquals(10.0, context.peek(), 0.001);
    }
    
    @Test
    public void testAddition() {
        calculator.executeCommand("PUSH 5");
        calculator.executeCommand("PUSH 3");
        calculator.executeCommand("+");
        assertEquals(8.0, context.peek(), 0.001);
    }
    
    @Test
    public void testSubtraction() {
        calculator.executeCommand("PUSH 10");
        calculator.executeCommand("PUSH 4");
        calculator.executeCommand("-");
        assertEquals(6.0, context.peek(), 0.001);
    }
    
    @Test
    public void testMultiplication() {
        calculator.executeCommand("PUSH 6");
        calculator.executeCommand("PUSH 7");
        calculator.executeCommand("*");
        assertEquals(42.0, context.peek(), 0.001);
    }
    
    @Test
    public void testDivision() {
        calculator.executeCommand("PUSH 10");
        calculator.executeCommand("PUSH 2");
        calculator.executeCommand("/");
        assertEquals(5.0, context.peek(), 0.001);
    }
    
    @Test
    public void testDivisionByZero() {
        calculator.executeCommand("PUSH 10");
        calculator.executeCommand("PUSH 0");
        calculator.executeCommand("/");
        assertEquals(2, context.getStack().size());
        assertEquals(0.0, context.peek(), 0.001);
        context.pop();
        assertEquals(10.0, context.peek(), 0.001);
    }
    
    @Test
    public void testSquareRoot() {
        calculator.executeCommand("PUSH 16");
        calculator.executeCommand("SQRT");
        assertEquals(4.0, context.peek(), 0.001);
    }
    
    @Test
    public void testSquareRootNegative() {
        calculator.executeCommand("PUSH -4");
        calculator.executeCommand("SQRT");
        assertEquals(-4.0, context.peek(), 0.001);
    }
    
    @Test
    public void testDefineParameter() {
        calculator.executeCommand("DEFINE a 10");
        calculator.executeCommand("PUSH a");
        assertEquals(10.0, context.peek(), 0.001);
    }
    
    @Test
    public void testComplexExpression() {
        calculator.executeCommand("PUSH 3");
        calculator.executeCommand("PUSH 5");
        calculator.executeCommand("+");
        calculator.executeCommand("PUSH 2");
        calculator.executeCommand("*");
        assertEquals(16.0, context.peek(), 0.001);
    }
    
    @Test
    public void testComments() {
        calculator.executeCommand("# This is a comment");
        calculator.executeCommand("PUSH 5 # Push value");
        assertEquals(5.0, context.peek(), 0.001);
    }
    
    @Test
    public void testFileExecution() throws IOException {
        String testFile = "test_commands.txt";
        String commands = "PUSH 10\nPUSH 20\n+\nPRINT\n";
        
        Files.write(Paths.get(testFile), commands.getBytes());
        
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        
        calculator.executeFile(testFile);
        
        assertTrue(outContent.toString().contains("30.0"));
        
        Files.delete(Paths.get(testFile));
        System.setOut(System.out);
    }
    
    @Test
    public void testStackUnderflow() {
        calculator.executeCommand("POP");
        assertTrue(context.getStack().isEmpty());
    }
}