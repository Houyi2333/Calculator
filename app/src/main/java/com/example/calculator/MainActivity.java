package com.example.calculator;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class MainActivity extends AppCompatActivity {
    private EditText editText;
    private TextView text;
    private Button button;
    private StringBuilder str = new StringBuilder();
    private int indexYN = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (getSupportActionBar() != null){
            getSupportActionBar().hide();
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editText = (EditText) findViewById(R.id.editView);
        text = (TextView) findViewById(R.id.textView);
    }

    //数字及符号按钮
    public void clickButton(View view){
        button = (Button) view;
        editText.append(button.getText());
        str.append(button.getText());
    }

    //清空
    public void empty(View view){
        editText.setText(null);
        str.delete(0,str.length());
    }

    //删除
    public void delete(View view){
        String nowText = editText.getText().toString();
        if(nowText.length()!=0&&str.length()!=0){
            editText.setText(nowText.substring(0,nowText.length()-1));
            str.deleteCharAt(str.length()-1);
        }
    }

    //等于
    public void equal(View view){
        indexYN = 0;
        System.out.println("str:\t"+str);
        System.out.println("lenth:\t"+str.length());
        estimate();
        if(indexYN==0){
            List<String> zhongZhui = zhuanZhongZhui(str.toString());
            System.out.println(zhongZhui);
            List<String> houZhui = zhuanHouZhui(zhongZhui);
            System.out.println(houZhui);
            editText.append("\n"+math(houZhui));
            str.delete(0,str.length());
            str.append(math(houZhui));
        }
    }

    //倒数
    public void reciprocal(View view){
        editText.append("1/");
        str.append("1/");
    }

    //阶乘
    public void factorial(View view){
        editText.append("!");
        str.append("!");
    }

    //平方
    public void square(View view){
        editText.append("^2");
        str.append("^2");
    }

    //立方
    public void cube(View view){
        editText.append("^3");
        str.append("^3");
    }

    //指数运算
    public void power(View view){
        editText.append("^");
        str.append("^");
    }

    //开方
    public void squareRoot(View view){
        editText.append("√");
        str.append("g");
    }

    //e
    public void eulerNumber(View view){
        editText.append("e");
        str.append("e");
    }

    //百分数
    public void percentage(View view){
        editText.append("%");
        str.append("*0.01");
    }

    //圆周率
    public void pi(View view){
        editText.append("π");
        str.append("p");
    }

    //sin
    public void sin(View view){
        editText.append("sin");
        str.append("s");
    }

    //cos
    public void cos(View view){
        editText.append("cos");
        str.append("c");
    }

    //tan
    public void tan(View view){
        editText.append("tan");
        str.append("t");
    }

    //ln
    public void ln(View view){
        editText.append("ln");
        str.append("l");
    }

    //log
    public void log(View view){
        editText.append("log");
        str.append("o");
    }

    private List<String> zhuanZhongZhui(String str) {
        //把输入的字符串转换成中缀表达式。存入list中
        int index = 0;
        List<String> list = new ArrayList<>();
        do{
            char ch = str.charAt(index);
            if("+-×/^!logsct()".indexOf(str.charAt(index)) >= 0){
                //是操作符，直接添加至list中
                index ++;
                list.add(ch+"");
            }else if (str.charAt(index) == 'e' || str.charAt(index) == 'p'){
                index ++;
                list.add(ch+"");
            } else if("0123456789".indexOf(str.charAt(index)) >= 0){
                //是数字,判断多位数的情况
                String str1 = "";
                while (index < str.length() && "0123456789.".indexOf(str.charAt(index)) >= 0){
                    str1 += str.charAt(index);
                    index ++;
                }
                list.add(str1);
            }
        }while (index < str.length());
        return list;
    }

    public List<String> zhuanHouZhui(List<String> list){//中缀表达式转换称后缀表达式
        Stack<String> fuZhan = new Stack<>();
        List<String> list2 = new ArrayList<>();
        if (!list.isEmpty()) {
            for (int i = 0; i < list.size(); i++) {
                if (isNumber(list.get(i))){
                    list2.add(list.get(i));
                } else if (list.get(i).charAt(0) == '('){
                    fuZhan.push(list.get(i));
                } else if (isOperator(list.get(i)) && list.get(i).charAt(0) != '('){
                    if (fuZhan.isEmpty()){
                        fuZhan.push(list.get(i));
                    } else {//符栈不为空
                        if (list.get(i).charAt(0) != ')'){
                            if (adv(fuZhan.peek()) <= adv(list.get(i))){
                                //入栈
                                fuZhan.push(list.get(i));
                            } else {//出栈
                                while (!fuZhan.isEmpty() && !"(".equals(fuZhan.peek())){
                                    if(adv(list.get(i)) <= adv(fuZhan.peek())){
                                        list2.add(fuZhan.pop());
                                    }
                                }
                                if (fuZhan.isEmpty() || fuZhan.peek().charAt(0) == '('){
                                    fuZhan.push(list.get(i));
                                }
                            }
                        } else if (list.get(i).charAt(0) == ')'){
                            while (fuZhan.peek().charAt(0) != '('){
                                list2.add(fuZhan.pop());
                            }
                            fuZhan.pop();
                        }
                    }
                }
            }
            while (!fuZhan.isEmpty()){
                list2.add(fuZhan.pop());
            }
        } else {
            editText.setText("");
        }
        return list2;
    }
    //判断是否为操作符
    public static boolean isOperator(String op){
        if ("0123456789.ep".indexOf(op.charAt(0)) == -1) {
            return true;
        } else {
            return false;
        }
    }
    //判断是否为操作数
    public static boolean isNumber(String num){
        if ("0123456789ep".indexOf(num.charAt(0)) >= 0) {
            return true;
        } else {
            return false;
        }
    }
    //判断操作符的优先级
    public static int adv(String f){
        int result = 0;
        switch(f) {
            case "+":
                result = 1;
                break;
            case "-":
                result = 1;
                break;
            case "×":
                result = 2;
                break;
            case "/":
                result = 2;
                break;
            case "^":
                result = 3;
                break;
            case "!":
                result = 4;
                break;
            case "g":
                result = 4;
                break;
            case "l":
                result = 4;
                break;
            case "o":
                result = 4;
                break;
            case "s":
                result = 4;
                break;
            case "c":
                result = 4;
                break;
            case "t":
                result = 4;
                break;
        }
        return result;
    }
    //通过后缀表达式进行计算
    public double math(List<String> list2) {
        Stack<String> stack = new Stack<String>();
        for (int i = 0; i < list2.size(); i++) {
            if (isNumber(list2.get(i))) {
                if (list2.get(i).charAt(0) == 'e'){
                    stack.push(String.valueOf(Math.E));
                } else if (list2.get(i).charAt(0) == 'p'){
                    stack.push(String.valueOf(Math.PI));
                } else {
                    stack.push(list2.get(i));
                }
            } else if (isOperator(list2.get(i))){
                double res = 0;
                if (list2.get(i).equals("+")) {
                    double num2 = Double.parseDouble(stack.pop());
                    double num1 = Double.parseDouble(stack.pop());
                    res = num1 + num2;
                } else if (list2.get(i).equals("-")) {
                    double num2 = Double.parseDouble(stack.pop());
                    double num1 = Double.parseDouble(stack.pop());
                    res = num1 - num2;
                } else if (list2.get(i).equals("×")) {
                    double num2 = Double.parseDouble(stack.pop());
                    double num1 = Double.parseDouble(stack.pop());
                    res = num1 * num2;
                } else if (list2.get(i).equals("/")) {
                    double num2 = Double.parseDouble(stack.pop());
                    double num1 = Double.parseDouble(stack.pop());
                    if (num2 != 0){
                        res = num1 / num2;
                    } else {
                        editText.setText("除数不能为0");
                        indexYN = 1;
                    }
                } else if (list2.get(i).equals("^")) {
                    double num2 = Double.parseDouble(stack.pop());
                    double num1 = Double.parseDouble(stack.pop());
                    res = Math.pow(num1, num2);
                } else if (list2.get(i).equals("!")) {
                    double num1 = Double.parseDouble(stack.pop());
                    if (num1 == 0 || num1 == 1){
                        res = 1;
                    } else if (num1 == (int)num1 && num1 > 1){
                        int d = 1;
                        for (int j = (int)num1; j >0; j--) {
                            d *= j;
                        }
                        res = d;
                    } else {
                        editText.setText("阶乘函数的定义域为自然数");
                        indexYN = 1;
                    }
                } else if (list2.get(i).equals("g")) {
                    double num1 = Double.parseDouble(stack.pop());
                    res = Math.sqrt(num1);
                } else if (list2.get(i).equals("l")) {
                    double num1 = Double.parseDouble(stack.pop());
                    if (num1 > 0){
                        res = Math.log(num1);
                    } else {
                        editText.setText("对数函数的定义域大于0");
                        indexYN = 1;
                    }
                } else if (list2.get(i).equals("o")) {
                    double num1 = Double.parseDouble(stack.pop());
                    if (num1 > 0){
                        res = Math.log(num1) / Math.log(2);
                    } else {
                        editText.setText("对数函数的定义域大于0");
                        indexYN = 1;
                    }
                } else if (list2.get(i).equals("s")) {
                    double num1 = Double.parseDouble(stack.pop());
                    res = Math.sin(num1);
                } else if (list2.get(i).equals("c")) {
                    double num1 = Double.parseDouble(stack.pop());
                    res = Math.cos(num1);
                } else if (list2.get(i).equals("t")) {
                    double num1 = Double.parseDouble(stack.pop());
                    if (Math.cos(num1) != 0){
                        res = Math.tan(num1);
                    } else {
                        editText.setText("注意正切函数的定义域");
                        indexYN = 1;
                    }
                }
                stack.push("" + res);
            }
        }
        if (indexYN == 0){
            if (!stack.isEmpty()){
                return Double.parseDouble(stack.pop());
            } else {
                return 0;
            }
        } else {
            return -999999;
        }
    }
    //判断输入是否错误
    public void estimate(){
        int i = 0;
        if (str.length() == 0){
            text.setText("输入不能为空！");
            indexYN = 1;
        }
        if (str.length() == 1){
            //当只有一位字符时，只能是“0123456789ep”中的一个
            if ("0123456789ep".indexOf(str.charAt(0)) == -1){
                text.setText("输入错误！");
                indexYN = 1;
            }
        }
        if (str.length() > 1){
            for (i = 0; i < str.length() - 1; i++) {
                //1.第一个字符只能为"losctg(0123456789ep"中的一个
                if ("losctg(0123456789ep".indexOf(str.charAt(0)) == -1){
                    text.setText("输入错误！");
                    indexYN = 1;
                }
                //2.“+-×/”后面只能是"0123456789losctg(ep"中的一个
                if ("+-×/".indexOf(str.charAt(i)) >= 0 && "0123456789losctg(ep".indexOf(str.charAt(i + 1)) == -1){
                    text.setText("输入错误！");
                    indexYN = 1;
                }
                //3."."后面只能是“0123456789”中的一个
                if (str.charAt(i) == '.' && "0123456789".indexOf(str.charAt(i + 1)) == -1){
                    text.setText("输入错误！");
                    indexYN = 1;
                }
                //4."!"后面只能是“+-×/^)”中的一个
                if (str.charAt(i) == '!' && "+-×/^)".indexOf(str.charAt(i + 1)) == -1){
                    text.setText("输入错误！");
                    indexYN = 1;
                }

                //5."losctg"后面只能是“0123456789(ep”中的一个
                if ("losctg".indexOf(str.charAt(i)) >= 0 && "0123456789(ep".indexOf(str.charAt(i + 1)) == -1){
                    text.setText("输入错误！");
                    indexYN = 1;
                }
                //6."0"的判断操作
                if (str.charAt(0) == '0' && str.charAt(1) == '0'){
                    text.setText("输入错误！");
                    indexYN = 1;
                }
                if (i >= 1 && str.charAt(i) == '0'){
                    //&& str.charAt(0) == '0' && str.charAt(1) == '0'
                    int m = i;
                    int n = i;
                    int is = 0;
                    //1.当0的上一个字符不为"0123456789."时，后一位只能是“+-×/.!^)”中的一个
                    if ("0123456789.".indexOf(str.charAt(m - 1)) == -1 && "+-×/.!^)".indexOf(str.charAt(i + 1)) == -1){
                        text.setText("输入错误！");
                        indexYN = 1;
                    }
                    //2.如果0的上一位为“.”,则后一位只能是“0123456789+-×/.^)”中的一个
                    if (str.charAt(m - 1) == '.' && "0123456789+-×/.^)".indexOf(str.charAt(i + 1)) == -1){
                        text.setText("输入错误！");
                        indexYN = 1;
                    }
                    n -= 1;
                    while (n > 0){
                        if ("(+-×/^glosct".indexOf(str.charAt(n)) >= 0){
                            break;
                        }
                        if (str.charAt(n) == '.'){
                            is++;
                        }
                        n--;
                    }

                    //3.如果0到上一个运算符之间没有“.”的话，整数位第一个只能是“123456789”，
                    //  且后一位只能是“0123456789+-×/.!^)”中的一个。
                    if ((is == 0 && str.charAt(n) == '0') || "0123456789+-×/.!^)".indexOf(str.charAt(i + 1)) == -1){
                        text.setText("输入错误！");
                        indexYN = 1;
                    }
                    //4.如果0到上一个运算符之间有一个“.”的话,则后一位只能是“0123456789+-×/.^)”中的一个
                    if (is == 1 && "0123456789+-×/.^)".indexOf(str.charAt(i + 1)) == -1){
                        text.setText("输入错误！");
                        indexYN = 1;
                    }
                    if (is > 1){
                        text.setText("输入错误！");
                        indexYN = 1;
                    }

                }
                //7."123456789"后面只能是“0123456789+-×/.!^)”中的一个
                if ("123456789".indexOf(str.charAt(i)) >= 0 && "0123456789+-×/.!^)".indexOf(str.charAt(i + 1)) == -1){
                    text.setText("输入错误！");
                    indexYN = 1;
                }
                //8."("后面只能是“0123456789locstg()ep”中的一个
                if (str.charAt(i) == '(' && "0123456789locstg()ep".indexOf(str.charAt(i + 1)) == -1){
                    text.setText("输入错误！");
                    indexYN = 1;
                }
                //9.")"后面只能是“+-×/!^)”中的一个
                if (str.charAt(i) == ')' && "+-×/!^)".indexOf(str.charAt(i + 1)) == -1){
                    text.setText("输入错误！");
                    indexYN = 1;
                }
                //10.最后一位字符只能是“0123456789!)ep”中的一个
                if ("0123456789!)ep".indexOf(str.charAt(str.length() - 1)) == -1){
                    text.setText("输入错误！");
                    indexYN = 1;
                }
                //12.不能有多个“.”
                if (i > 2 && str.charAt(i) == '.'){
                    int n = i - 1;
                    int is = 0;
                    while (n > 0){
                        if ("(+-×/^glosct".indexOf(str.charAt(n)) >= 0){
                            break;
                        }
                        if (str.charAt(n) == '.'){
                            is++;
                        }
                        n--;
                    }
                    if (is > 0){
                        text.setText("输入错误！");
                        indexYN = 1;
                    }
                }
                //13."ep"后面只能是“+-×/^)”中的一个
                if ("ep".indexOf(str.charAt(i)) >= 0 && "+-×/^)".indexOf(str.charAt(i + 1)) == -1){
                    text.setText("输入错误！");
                    indexYN = 1;
                }
            }
        }
    }
}