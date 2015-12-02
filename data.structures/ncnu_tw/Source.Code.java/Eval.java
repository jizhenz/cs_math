/**
 * Purpose: using Stack to evaluate infix expression
 * Author: Shiuh-Sheng Yu
 * Since 2008/11/10
 */
import java.util.Scanner;
import java.io.*;
public class Eval {
    private Expression exp;
    private Stack operandStack, operatorStack;
    private boolean hasError;
    //plus, minus, mul, div, mode, power, fun, lb
    public static final int[] icp = {5,5,6,6,6,8,18,20};
    public static final int[] isp = {5,5,6,6,6,7,18, 1};
    public Eval(String s) {
        exp = new Expression(s);
        operandStack = new Stack();
        operatorStack = new Stack();
        Token next;
        while ((next = exp.nextToken()).type != Token.END && next.type != Token.ERROR) {
            if (next.type == Token.RB) {
                while (!operatorStack.isEmpty() && operatorStack.peek() != Token.LB) {
                    exec((Token)operatorStack.pop());
                }
                if (operatorStack.isEmpty()) {
                    System.out.println("unbalanced ()");
                    return;
                }
                operatorStack.pop();
            } else if (next.type == Token.LIT) {
                operandStack.push(new Double(next.data));
            } else {
                while (!operatorStack.isEmpty() && isp[operatorStack.peek()] >= icp[next.type]) {
                    exec((Token)operatorStack.pop());
                }
                operatorStack.push(next);
            }
        }
        if (next.type == Token.ERROR) {
            System.out.println("Illegal expression");
            return;
        }
        while (!operatorStack.isEmpty()) {
            exec((Token)operatorStack.pop());
        }
        if (operandStack.size() != 1 || hasError) {
            System.out.println("Illegal expression");
            return;
        }
        System.out.println(((Double)operandStack.pop()).doubleValue());
    }
    private void exec(Token token) {
        double op1, op2;
        if (operandStack.size() == 0 || (token.type!=Token.FUN && operandStack.size()<2)){
            hasError = true;
            return;
        }
        switch(token.type) {
        case Token.PLUS:
            op2 = ((Double)operandStack.pop()).doubleValue();
            op1 = ((Double)operandStack.pop()).doubleValue();
            operandStack.push(new Double(op1+op2));
            break;
        case Token.MINUS:
            op2 = ((Double)operandStack.pop()).doubleValue();
            op1 = ((Double)operandStack.pop()).doubleValue();
            operandStack.push(new Double(op1-op2));
            break;
        case Token.MUL:
            op2 = ((Double)operandStack.pop()).doubleValue();
            op1 = ((Double)operandStack.pop()).doubleValue();
            operandStack.push(new Double(op1*op2));
            break;
        case Token.DIV:
            op2 = ((Double)operandStack.pop()).doubleValue();
            op1 = ((Double)operandStack.pop()).doubleValue();
            operandStack.push(new Double(op1/op2));
            break;
        case Token.MODE:
            op2 = ((Double)operandStack.pop()).doubleValue();
            op1 = ((Double)operandStack.pop()).doubleValue();
            operandStack.push(new Double(op1%op2));
            break;
        case Token.POWER:
            op2 = ((Double)operandStack.pop()).doubleValue();
            op1 = ((Double)operandStack.pop()).doubleValue();
            operandStack.push(new Double(Math.pow(op1,op2)));
            break; 
        case Token.FUN:
            op1 = ((Double)operandStack.pop()).doubleValue();
            if (token.data.equals("sin")) {
                operandStack.push(new Double(Math.sin(op1)));
            } else if (token.data.equals("cos")) {
                operandStack.push(new Double(Math.cos(op1)));
            } else if (token.data.equals("tan")) {
                operandStack.push(new Double(Math.tan(op1)));
            } else if (token.data.equals("asin")) {
                operandStack.push(new Double(Math.asin(op1)));
            } else if (token.data.equals("acos")) {
                operandStack.push(new Double(Math.acos(op1)));
            } else if (token.data.equals("atan")) {
                operandStack.push(new Double(Math.atan(op1)));
            } else if (token.data.equals("sqrt")) {
                operandStack.push(new Double(Math.sqrt(op1)));
            } else if (token.data.equals("cbrt")) {
                operandStack.push(new Double(Math.cbrt(op1)));
            } else if (token.data.equals("exp")) {
                operandStack.push(new Double(Math.exp(op1)));
            } else if (token.data.equals("log")) {
                operandStack.push(new Double(Math.log(op1)));
            } else {
                hasError = true;
            }
            break;
        default:
            hasError = true;
        }
    }
    public static void main(String[] argv) {
        Scanner input = new Scanner(System.in);
        while (input.hasNext()) {
            new Eval(input.nextLine());
        }
    }
}
class Stack {
    Object[] data = new Object[100];
    int top;
    public void push(Object x) {
        data[top++] = x;
    }
    public Object pop() {
        return data[--top];
    }
    public int peek() {
        if (top == 0) return Token.END;
        return ((Token)data[top-1]).type;
    }
    public int size() {
        return top;
    }
    public boolean isEmpty() {
        return top==0;
    }
}
class Token {
    public static final int PLUS  = 0;
    public static final int MINUS = 1;
    public static final int MUL   = 2;
    public static final int DIV   = 3;
    public static final int MODE  = 4;
    public static final int POWER = 5; // ^
    public static final int FUN   = 6;
    public static final int LB    = 7; // (
    public static final int RB    = 8; // )
    public static final int END   = 9;
    public static final int LIT   = 10; // number
    public static final int ERROR = 11;
    int type;
    String data;
    Token(int t, String d) {
        type = t;
        data = d;
    }
}
class Expression {
    private int ptr;
    private String data;
    private boolean wantOperand = true;
    public Expression(String s) {
        ptr = 0;
        data = s;
    }
    public Token nextToken() {
        int i;
        for (i = ptr; i < data.length() && data.charAt(i)==' '; i++) {
        }
        ptr = i;
        if (i == data.length()) {
            return new Token(Token.END,"");
        }
        switch (data.charAt(i)) {
        case '+':
            ptr++;
            wantOperand = true;
            return new Token(Token.PLUS,"+");
        case '*':
            ptr++;
            wantOperand = true;
            return new Token(Token.MUL, "*");
        case '/':
            ptr++;
            wantOperand = true;
            return new Token(Token.DIV, "/");
        case '%':
            ptr++;
            wantOperand = true;
            return new Token(Token.MODE,"%");
        case '^':
            ptr++;
            wantOperand = true;
            return new Token(Token.POWER,"^");
        case '(':
            ptr++;
            wantOperand = true;
            return new Token(Token.LB,"(");
        case ')':
            ptr++;
            wantOperand = false;
            return new Token(Token.RB,")");
        default:
            if (data.charAt(i) == '-' && !wantOperand) {
                ptr++;
                wantOperand = true;
                return new Token(Token.MINUS,"-");
            }
            StringBuilder buf = new StringBuilder();
            if (data.charAt(i)=='-'
                ||(data.charAt(i)>='0' && data.charAt(i)<='9')
                || data.charAt(i)=='.') {
                wantOperand = false;
                if (data.charAt(i) == '-') {
                    buf.append(data.charAt(i++));
                }
                while (i<data.length()
                       && data.charAt(i)>='0'
                       && data.charAt(i)<='9') {
                    buf.append(data.charAt(i++));
                }
                if (i<data.length() && data.charAt(i) == '.') {
                    buf.append(data.charAt(i++));
                    while (i<data.length()
                        && data.charAt(i)>='0'
                        && data.charAt(i)<='9') {
                        buf.append(data.charAt(i++));
                    }
                }
                if (i<data.length() && data.charAt(i) == 'E') {
                    buf.append(data.charAt(i++));
                    if (i<data.length() && data.charAt(i) == '-') {
                        buf.append(data.charAt(i++));
                    }
                    while (i<data.length()
                        && data.charAt(i)>='0'
                        && data.charAt(i)<='9') {
                        buf.append(data.charAt(i++));
                    }
                }
                ptr = i;
                if (buf.length()==0) {
                    return new Token(Token.ERROR,"");
                } else {
                    return new Token(Token.LIT,buf.toString());
                }
            } else {
                // FUN
                while(i<data.length()
                    && data.charAt(i)>='a'
                    && data.charAt(i)<='z') {
                    buf.append(data.charAt(i++));
                }
                ptr = i;
                if (buf.length()==0) {
                    return new Token(Token.ERROR, "");
                } else if (i>=data.length()) {
                    return new Token(Token.ERROR, "");
                } else {
                    return new Token(Token.FUN, buf.toString());
                }
            }
        }
    }
}