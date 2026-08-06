package calculator.context;

import java.util.*;

public class ExecutionContext {
    private final Deque<Double> stack;
    private final Map<String, Double> parameters;
    
    public ExecutionContext() {
        this.stack = new ArrayDeque<>();
        this.parameters = new HashMap<>();
    }
    
    public Deque<Double> getStack() {
        return stack;
    }
    
    public Map<String, Double> getParameters() {
        return parameters;
    }
    
    public void push(double value) {
        stack.push(value);
    }
    
    public double pop() {
        if (stack.isEmpty()) {
            throw new EmptyStackException();
        }
        return stack.pop();
    }
    
    public double peek() {
        if (stack.isEmpty()) {
            throw new EmptyStackException();
        }
        return stack.peek();
    }
    
    public void define(String name, double value) {
        parameters.put(name, value);
    }
    
    public Double getParameter(String name) {
        return parameters.get(name);
    }
    
    public boolean hasParameter(String name) {
        return parameters.containsKey(name);
    }
}