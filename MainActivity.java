package com.example.thinkpad.cal.feature;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.Stack;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Button btn0, btn1, btn2, btn3, btn4, btn5, btn6, btn7, btn8, btn9, btnPt;
    Button btnDiv, btnMul, btnMin, btnSub, btnPercent;
    Button btnClr, btnBack, btnBracket, btnResult;
    EditText etInput;
    int brc_flag = 0;   //未匹配的左括号的个数

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn0 = findViewById(R.id.btn0);
        btn1 = findViewById(R.id.btn1);
        btn2 = findViewById(R.id.btn2);
        btn3 = findViewById(R.id.btn3);
        btn4 = findViewById(R.id.btn4);
        btn5 = findViewById(R.id.btn5);
        btn6 = findViewById(R.id.btn6);
        btn7 = findViewById(R.id.btn7);
        btn8 = findViewById(R.id.btn8);
        btn9 = findViewById(R.id.btn9);
        btnPt = findViewById(R.id.btnPt);
        btnDiv = findViewById(R.id.btnDiv);
        btnMul = findViewById(R.id.btnMul);
        btnMin = findViewById(R.id.btnMin);
        btnSub = findViewById(R.id.btnSub);
        btnPercent = findViewById(R.id.btnPercent);
        btnClr = findViewById(R.id.btnClr);
        btnBack = findViewById(R.id.btnBack);
        btnBracket = findViewById(R.id.btnBracket);
        btnResult = findViewById(R.id.btnResult);
        etInput = findViewById(R.id.etInput);

        btn0.setOnClickListener(this);
        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
        btn3.setOnClickListener(this);
        btn4.setOnClickListener(this);
        btn5.setOnClickListener(this);
        btn6.setOnClickListener(this);
        btn7.setOnClickListener(this);
        btn8.setOnClickListener(this);
        btn9.setOnClickListener(this);
        btnPt.setOnClickListener(this);
        btnDiv.setOnClickListener(this);
        btnMul.setOnClickListener(this);
        btnMin.setOnClickListener(this);
        btnSub.setOnClickListener(this);
        btnPercent.setOnClickListener(this);
        btnClr.setOnClickListener(this);
        btnBack.setOnClickListener(this);
        btnBracket.setOnClickListener(this);
        btnResult.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        String str = etInput.getText().toString();
        int i = v.getId();
        if (str.equals("Error!"))
            str = "";
        if (str.length() > 3)
            if (str.charAt(str.length() - 3) == 'E')
                str = "";
        if (i == R.id.btn0 || i == R.id.btn1 || i == R.id.btn2 || i == R.id.btn3 || i == R.id.btn4 || i == R.id.btn5 || i == R.id.btn6 || i == R.id.btn7 || i == R.id.btn8 || i == R.id.btn9 || i == R.id.btnPt) {
            if (str.equals("0"))
                str = "";
            if (str.length() > 0)
                if (str.charAt(str.length() - 1) == '0')
                    str = str.substring(0, str.length() - 1);
            etInput.setText(str + ((Button) v).getText());
        } else if (i == R.id.btnDiv || i == R.id.btnMul || i == R.id.btnMin || i == R.id.btnSub || i == R.id.btnPercent) {
            if (str.length() > 0) {
                char s = str.charAt(str.length() - 1);
                if (s == '+' || s == '-' || s == '×' || s == '÷')
                    etInput.setText(str.substring(0, str.length() - 1) + ((Button) v).getText());
                else if (s == '(')
                    etInput.setText(str);
                else
                    etInput.setText(str + ((Button) v).getText());
            }
        } else if (i == R.id.btnBracket) {
            if (str.length() == 0) {
                etInput.setText(String.valueOf('('));
                brc_flag++;
            } else {
                char s = str.charAt(str.length() - 1);
                if ((s >= '0' & s <= '9') || s == ')' || s == '%')
                    if (brc_flag == 0) {
                        etInput.setText(str + String.valueOf('×') + String.valueOf('('));
                        brc_flag++;
                    } else {
                        etInput.setText(str + String.valueOf(')'));
                        brc_flag--;
                    }
                else if (s == '+' || s == '-' || s == '×' || s == '÷' || s == '(') {
                    etInput.setText(str + String.valueOf('('));
                    brc_flag++;
                }
            }
        } else if (i == R.id.btnClr) {
            str = "";
            etInput.setText("");
        } else if (i == R.id.btnBack) {
            if (!str.equals(""))
                etInput.setText(str.substring(0, str.length() - 1));
        } else if (i == R.id.btnResult) {
            if ((str.charAt(str.length() - 1) < '0' || str.charAt(str.length() - 1) > '9') && str.charAt(str.length() - 1) != ')' && str.charAt(str.length() - 1) != '%')
                etInput.setText(str.substring(0, str.length() - 1));
            etInput.setText(getResult());
        }
    }

    private int judgeChar(char ch) {
        int i = 6;
        switch (ch) {
            case '+':
                i = 0;
                break;
            case '-':
                i = 1;
                break;
            case '×':
                i = 2;
                break;
            case '÷':
                i = 3;
                break;
            case '(':
                i = 4;
                break;
            case ')':
                i = 5;
                break;
        }
        return i;
    }

    private char precedeOps(char ch1, char ch2) {
        char[][] a = {{'>', '>', '<', '<', '<', '>'},
                {'>', '>', '<', '<', '<', '>'},
                {'>', '>', '>', '>', '<', '>'},
                {'>', '>', '>', '>', '<', '>'},
                {'<', '<', '<', '<', '<', '='},
                {'>', '>', '>', '>', ' ', '>'}};
        return a[judgeChar(ch1)][judgeChar(ch2)];
    }

    private String getResult() {
        Stack<String> NUM = new Stack<>();
        Stack<Character> OPS = new Stack<>();
        String exp = etInput.getText().toString();
        boolean pt_flag = false;  //判断是否是小数
        if (exp.equals("")) return "Error!";
        if ((exp.charAt(exp.length() - 1) < '0' || exp.charAt(exp.length() - 1) > '9') && exp.charAt(exp.length() - 1) != ')' && exp.charAt(exp.length() - 1) != '%')
            return "Error!";
        double num = 0;
        int i;
        for (i = 0; i < exp.length(); i++) {
            char ch = exp.charAt(i);
            switch (ch) {
                case '0':
                case '1':
                case '2':
                case '3':
                case '4':
                case '5':
                case '6':
                case '7':
                case '8':
                case '9':
                    if (NUM.empty())
                        NUM.push(String.valueOf(ch - '0'));
                    else {
                        if (pt_flag) {
                            if (NUM.peek().charAt(NUM.peek().length() - 1) == '0')
                                num = Double.parseDouble(NUM.peek().substring(0, NUM.peek().length() - 1) + ch);
                            else
                                num = Double.parseDouble(NUM.peek() + ch);
                            NUM.pop();
                        } else {
                            if (exp.charAt(i - 1) >= '0' && exp.charAt(i - 1) <= '9') {
                                num = Double.parseDouble(NUM.peek()) * 10 + ch - '0';
                                NUM.pop();
                            } else
                                num = ch - '0';
                        }
                        NUM.push(String.valueOf(num));
                    }
                    break;
                case '.':
                    pt_flag = true;
                    break;
                case '%':
                    num = Double.parseDouble(NUM.peek());
                    NUM.pop();
                    NUM.push(calculate(num));
                    if (i != exp.length() - 1)
                        if (exp.charAt(i + 1) >= '0' && exp.charAt(i + 1) <= '9')
                            OPS.push('×');
                    pt_flag = false;
                    break;
                case '×':
                case '÷':
                case '+':
                case '-':
                case '(':
                case ')':
                    if (OPS.empty())
                        OPS.push(ch);
                    else {
                        char op = OPS.peek();
                        char i1 = precedeOps(op, ch);
                        if (i1 == '<')
                            OPS.push(ch);
                        else if (i1 == '=')
                            OPS.pop();
                        else if (i1 == '>') {
                            double n2 = Double.parseDouble(NUM.peek());
                            NUM.pop();
                            double n1 = Double.parseDouble(NUM.peek());
                            NUM.pop();
                            OPS.pop();
                            OPS.push(ch);
                            NUM.push(calculate(n1, op, n2));
                        }
                        pt_flag = false;
                    }
                    if(OPS.peek()==')') {
                        OPS.pop();
                        while (OPS.peek()!='('){
                            double n2 = Double.parseDouble(NUM.peek());
                            NUM.pop();
                            double n1 = Double.parseDouble(NUM.peek());
                            NUM.pop();
                            NUM.push(calculate(n1, OPS.peek(), n2));
                            OPS.pop();
                        }
                        OPS.pop();
                    }
                    break;
            }
            if (!NUM.empty())
                if (NUM.peek().equals("Error!"))
                    return "Error!";
        }
        while (!OPS.empty()) {
            if (OPS.size() > 1) {
                char ch = OPS.peek();
                OPS.pop();
                char op = OPS.peek();
                char i1 = precedeOps(op, ch);
                if (i1 == '<')
                    OPS.push(ch);
                else if (i1 == '=')
                    OPS.pop();
                else if (i1 == '>') {
                    double n3 = Double.parseDouble(NUM.peek());
                    NUM.pop();
                    double n2 = Double.parseDouble(NUM.peek());
                    NUM.pop();
                    double n1 = Double.parseDouble(NUM.peek());
                    NUM.pop();
                    OPS.pop();
                    OPS.push(ch);
                    NUM.push(calculate(n1, op, n2));
                    NUM.push(String.valueOf(n3));
                }
            } else {
                char op = OPS.peek();
                OPS.pop();
                double n2 = Double.parseDouble(NUM.peek());
                NUM.pop();
                double n1 = Double.parseDouble(NUM.peek());
                NUM.pop();
                NUM.push(calculate(n1, op, n2));
            }
        }
        if (NUM.peek().equals("Error!"))
            return "Error!";
        else {
            double re = Double.parseDouble(NUM.peek());
            if (re == (double) (int) re)
                return String.valueOf((int) re);
            else
                return NUM.peek();
        }
    }

    private String calculate(double n1, char op, double n2) {
        double result = 0;
        switch (op) {
            case '+':
                result = n1 + n2;
                break;
            case '-':
                result = n1 - n2;
                break;
            case '×':
                result = n1 * n2;
                break;
            case '÷':
                if (n2 == 0)
                    return "Error!";
                else
                    result = n1 / n2;
                break;
        }
        return String.valueOf(result);
    }

    private String calculate(double n) {
        return String.valueOf(n / 100.0);
    }
}