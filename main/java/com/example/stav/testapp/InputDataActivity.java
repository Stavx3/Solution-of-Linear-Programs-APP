package com.example.stav.testapp;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.ScrollingMovementMethod;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Scroller;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Vector;


import static com.example.stav.testapp.R.layout.activity_input_data;

public class InputDataActivity extends AppCompatActivity {

    public static final String ANS_KEY="com.example.stav.testapp.ANS_KEY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(activity_input_data);


        Intent intent = getIntent();
        String outputVar = intent.getStringExtra(MainActivity.VAR_KEY);
        String outputCon = intent.getStringExtra(MainActivity.CON_KEY);
        final String minmax = intent.getStringExtra(MainActivity.MINMAX_KEY);
        final int Cons = Integer.parseInt(outputCon);
        final int Vars = Integer.parseInt(outputVar);

        final Vector <Vector <EditText>> Numbers =new Vector<Vector<EditText>>();

        InputNum(Vars, "Enter Z equation", Numbers, 0);
        for (int i = 0; i < Cons; i++)
            InputNum(Vars, "Enter Constraint Number " + (i+1), Numbers, i + 1);

        Button next = new Button(this);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        next.setLayoutParams(lp);
        next.setText("Done");
        LinearLayout ll = (LinearLayout) findViewById(R.id.linlay);
        ll.addView(next);

        next.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Matala(Numbers,Vars,Cons,minmax);
            }
        });


    }

    public void InputNum(int Vars, String details, Vector<Vector<EditText>> Numbers, int eqNumber) {
        //getting the main layout
        LinearLayout ll = (LinearLayout) findViewById(R.id.linlay);
        //define different layouts
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        WindowManager.LayoutParams layouts = new WindowManager.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layouts.height = 150;
        //edittext and textview layout.
        WindowManager.LayoutParams box = new WindowManager.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        box.width = 100;
        box.height = 150;
        HorizontalScrollView scroll = new HorizontalScrollView(this);
        LinearLayout l = new LinearLayout(this);
        scroll.setLayoutParams(lp);
        l.setLayoutParams(layouts);
        l.setOrientation(LinearLayout.HORIZONTAL);
        TextView des = new TextView(this);
        des.setText(details);
        l.addView(des, lp);

        int index = 1;
        TextView tv = new TextView(this);
        EditText et = new EditText(this);


        if (eqNumber == 0) {
            tv.setText("Z= ");
            l.addView(tv, box);
        }

        Vector<EditText> eq=new Vector<EditText>();
        Numbers.add(eq);
        for (int j = 0; j < Vars; j++) {
            tv = new TextView(this);
            et = new EditText(this);
            et.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_NUMBER_FLAG_SIGNED);
            index = j + 1;
            tv.setText("X" + index);
            l.addView(et, box);
            l.addView(tv, box);
            eq.add(et);
            if ((j == Vars - 1)&& (eqNumber!=0)) {
                tv = new TextView(this);
                et = new EditText(this);
                et.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_NUMBER_FLAG_SIGNED);
                tv.setText("RHS");
                l.addView(et, box);
                l.addView(tv, box);
                eq.add(et);
            }
        }
        l.setBackgroundResource(R.drawable.white_square);
        scroll.addView(l);
        ll.addView(scroll);

    }


    public  void Matala(Vector<Vector<EditText>> Numbers, int Vars, int Cons, String minmax) {

        boolean ans = false;
        double RHSfinal = 0;
        boolean manysol = false;
        boolean menovansol = false;

        int type;
        System.out.println("Please enter 0 if the function type is min and 1 if max");
        if (minmax.equals("Minimum")) type = 0;
        else type = 1;
        System.out.println("Plese enter the number of decision variables");
        int des = Vars;
        System.out.println("Plese enter the number of constraint variables");
        int con = Cons;

        double Zfun[] = new double[des + con];
        double Mat[][] = new double[con + 1][des + con + 1];
        for (int i = 0; i < des; i++) {
            System.out.println("Plese enter the Z function variable number " + (i + 1));
            double temp=Double.parseDouble(Numbers.get(0).get(i).getText().toString());
            System.out.print(temp);
            Mat[0][i] = -temp;
            Zfun[i] = temp;
        }

        for (int i = 1; i <= con; i++) {
            for (int j = 0; j < des; j++) {
                System.out.println("Plese enter the coefficient of " + (j + 1) + "the decision variables of the " + (i) + "constraint");
                double temp=Double.parseDouble(Numbers.get(i).get(j).getText().toString());
                Mat[i][j] = temp;
            }

            System.out.println("Plese enter the RHS of " + (i) + "constraint");
            double RHS=Double.parseDouble(Numbers.get(i).get(Vars).getText().toString());
            Mat[i][Mat[0].length - 1] = RHS;
        }

        int i = Mat.length - 1;
        int j = Mat[0].length - 2;

        for (int k = 0; k < con; k++) {
            Mat[i][j] = 1;
            i--;
            j--;
        }

        int CBindex[] = new int[con];
        String BaseS[]= new String[con];
        String allStrings[]=new String[con+des];

        for (int k = des; k < Mat[0].length - 1; k++) {
            CBindex[k - des] = k;
        }

        for(int k=0; k<con; k++){
            BaseS[k]="S"+Integer.toString(k+1);
            allStrings[des+k]="S"+Integer.toString(k+1);
        }

        for(int k=0; k<des; k++){
            allStrings[k]="X"+Integer.toString(k+1);
        }




        double CB[] = new double[con];
        double Btemp[][] = new double[con][con];
        double B[][] = new double[con][con];
        double Binverse[][] = new double[B.length][B[0].length];
        double y[] = new double[con];
        double A[][] = new double[con][des];
        double First[][] = new double[con][des + con];
        for (int k = 0; k < First.length; k++) {
            for (int k2 = 0; k2 < First[0].length; k2++) {
                First[k][k2] = Mat[k + 1][k2];
            }
        }

        for (int k = 0; k < A[0].length; k++) {
            for (int k2 = 0; k2 < A.length; k2++) {
                A[k2][k] = Mat[k2 + 1][k];
            }
        }

        double Ay[] = new double[des];
        double c[] = new double[des];
        for (int k = 0; k < c.length; k++) {
            c[k] = Mat[0][k] * (-1);
        }
        double CAY[] = new double[c.length];
        double b[] = new double[con];
        for (int k = 0; k < b.length; k++) {
            b[k] = Mat[k + 1][Mat[0].length - 1];
        }
        double Base[] = new double[con];
        double BA[][] = new double[Binverse.length][A[0].length];

        while (!ans) {
            double max = Mat[0][0];
            double min = Mat[0][0];
            int index = 0;

            if (type == 0) {
                for (int k = 1; k < Mat[0].length - 1; k++) {
                    if (Mat[0][k] > max) {
                        index = k;
                        max = Mat[0][k];
                    }
                }

            } else {
                for (int k = 1; k < Mat[0].length - 1; k++) {
                    if (Mat[0][k] < min) {
                        index = k;
                        min = Mat[0][k];
                    }
                }
            }
            min = Mat[1][Mat[0].length - 1] / Mat[1][index];
            if (min < 0) min = Double.POSITIVE_INFINITY;
            int indexRow = 1;
            double sum = 0;
            for (int k = 2; k < Mat.length; k++) {
                sum = Mat[k][Mat[0].length - 1] / Mat[k][index];
                if (min > sum && sum >= 0) {
                    indexRow = k;
                    min = sum;
                }

            }

            CBindex[indexRow - 1] = index;
            for (int k = 0; k < CBindex.length; k++) {
                CB[k] = Zfun[CBindex[k]];
            }


            for (int k2 = 0; k2 < B.length; k2++) {
                for (int k = 0; k < B[0].length; k++) {
                    B[k2][k] = First[k2][CBindex[k]];
                }
            }

            BaseS[indexRow-1]=allStrings[index];

            for (int k = 0; k < Btemp.length; k++) {
                for (int k2 = 0; k2 < Btemp[0].length; k2++) {
                    Btemp[k][k2] = B[k][k2];
                }
            }
            Binverse = invert(Btemp);

            BA = multiply(Binverse, A);

            for (int k = 0; k < BA.length; k++) {
                for (int k2 = 0; k2 < BA[0].length; k2++) {
                    Mat[k + 1][k2] = BA[k][k2];
                }
            }

            for (int k = 0; k < Binverse.length; k++) {
                for (int k2 = 0; k2 < Binverse[0].length; k2++) {
                    Mat[k + 1][k2 + des] = Binverse[k][k2];
                }
            }
            y = multiplyWithForLoops(Binverse, CB);
            Ay = multiplyWithForLoops(A, y);

            for (int k = 0; k < CAY.length; k++) {
                CAY[k] = Ay[k] - c[k];
            }


            double sum1 = 0;
            for (int k = 0; k < b.length; k++) {
                sum1 += b[k] * y[k];
            }
            RHSfinal = sum1;
            Mat[0][Mat[0].length - 1] = RHSfinal;

            boolean boolCAY = true;
            boolean booly = true;
            boolean boolbB = true;

            int k;
            if (type == 0) {
                for (k = 0; k < CAY.length; k++) {
                    Mat[0][k] = CAY[k];
                    if (CAY[k] > 0) boolCAY = false;
                }
                for (int j1 = k; j1 < y.length + k; j1++) {
                    Mat[0][j1] = y[j1 - k];
                    if (y[j1 - k] > 0) booly = false;
                }
                Base = multiplyWithForLoops1(Binverse, b);
                for (int k2 = 0; k2 < b.length; k2++) {
                    Mat[k2 + 1][Mat[0].length - 1] = Base[k2];
                    if (Base[k2] > 0) boolbB = false;
                }
            } else {
                for (k = 0; k < CAY.length; k++) {
                    Mat[0][k] = CAY[k];
                    if (CAY[k] < 0) boolCAY = false;
                }
                for (int j1 = k; j1 < y.length + k; j1++) {
                    Mat[0][j1] = y[j1 - k];
                    if (y[j1 - k] < 0) booly = false;
                }
                Base = multiplyWithForLoops1(Binverse, b);
                for (int k2 = 0; k2 < b.length; k2++) {
                    Mat[k2 + 1][Mat[0].length - 1] = Base[k2];
                    if (Base[k2] < 0) boolbB = false;
                }
            }

            if (boolCAY && booly && boolbB) ans = true;
        }
        boolean isBase = false;
        for (int k = 0; k < Mat[0].length - 1; k++) {
            for (int k2 = 0; k2 < CBindex.length; k2++) {
                if (k == CBindex[k2]) isBase = true;
            }
            if (!isBase && Mat[0][k] == 0) manysol = true;
            isBase = false;
        }
        for (int k = 0; k < Base.length; k++) {
            if (Base[k] == 0) menovansol = true;
        }

        Intent intent=new Intent(this,OutputDataActivity.class);
        String tvAns="";
        if (manysol){
            //System.out.println("There are many solutions to this function");
            tvAns=tvAns+"There are many solutions to this function";
        }
        else if (menovansol) {
            //System.out.println("The solution is menovan");
            tvAns=tvAns+"Degenerate solution";
        }
        else {
            String temp="";
            for (int k=0; k<Base.length; k++){
                temp= temp+BaseS[k] +"= "+ Base[k]+"\n";
            }
            boolean isin=false;
            for(int k=0; k<allStrings.length; k++){
                for(int k1=0; k1<BaseS.length; k1++){
                    if(allStrings[k].equals(BaseS[k1]))isin=true;
                }
                if(!isin)temp=temp+allStrings[k]+"= "+0.0+"\n";
                isin=false;
            }

            tvAns= "Z= "+RHSfinal+"\n"+temp;

            /*System.out.println("Z value is " + RHSfinal);
            System.out.println(Arrays.toString(Base));
            for (int k=0; k<Base.length; k++){
                System.out.println(BaseS[k] +"= "+ Base[k]);
            }
            System.out.println("B Matrix: ");
            for (int k = 0; k < B.length; k++) {
                for (int k2 = 0; k2 < B[0].length; k2++) {
                    System.out.print(B[k][k2] + " ");
                }
                System.out.println();
            }

            System.out.println("B^-1 Matrix: ");
            for (int k = 0; k < Binverse.length; k++) {
                for (int k2 = 0; k2 < Binverse[0].length; k2++) {
                    System.out.print(Binverse[k][k2] + " ");
                }
                System.out.println();
            }
            */
        }
        intent.putExtra(ANS_KEY,tvAns);
        startActivity(intent);
    }


    public static double[][] multiply(double[][] a, double[][] b) {
        int m1 = a.length;
        int n1 = a[0].length;
        int m2 = b.length;
        int n2 = b[0].length;
        if (n1 != m2) throw new RuntimeException("Illegal matrix dimensions.");
        double[][] c = new double[m1][n2];
        for (int i = 0; i < m1; i++)
            for (int j = 0; j < n2; j++)
                for (int k = 0; k < n1; k++)
                    c[i][j] += a[i][k] * b[k][j];
        return c;
    }


    public static double[] multiplyWithForLoops1(double[][] matrix, double[] vector) {
        int rows = matrix.length;
        int columns = matrix[0].length;

        double ans[] = new double[rows];

        for (int i = 0; i < rows; i++) {
            double sum = 0;
            for (int j = 0; j < columns; j++) {
                sum = sum + vector[j] * matrix[i][j];
            }
            ans[i] = sum;
        }
        return ans;

    }

    public static double[] multiplyWithForLoops(double[][] matrix, double[] vector) {
        int rows = matrix.length;
        int columns = matrix[0].length;

        double ans[] = new double[columns];

        for (int i = 0; i < columns; i++) {
            double sum = 0;
            for (int j = 0; j < rows; j++) {
                sum = sum + vector[j] * matrix[j][i];
            }
            ans[i] = sum;
        }
        return ans;

    }


    public static double[][] invert(double a[][]) {
        int n = a.length;
        double x[][] = new double[n][n];
        double b[][] = new double[n][n];
        int index[] = new int[n];
        for (int i = 0; i < n; ++i)
            b[i][i] = 1;

        // Transform the matrix into an upper triangle
        gaussian(a, index);

        // Update the matrix b[i][j] with the ratios stored
        for (int i = 0; i < n - 1; ++i)
            for (int j = i + 1; j < n; ++j)
                for (int k = 0; k < n; ++k)
                    b[index[j]][k]
                            -= a[index[j]][i] * b[index[i]][k];

        // Perform backward substitutions
        for (int i = 0; i < n; ++i) {
            x[n - 1][i] = b[index[n - 1]][i] / a[index[n - 1]][n - 1];
            for (int j = n - 2; j >= 0; --j) {
                x[j][i] = b[index[j]][i];
                for (int k = j + 1; k < n; ++k) {
                    x[j][i] -= a[index[j]][k] * x[k][i];
                }
                x[j][i] /= a[index[j]][j];
            }
        }
        return x;
    }

    // Method to carry out the partial-pivoting Gaussian
    // elimination.  Here index[] stores pivoting order.

    public static void gaussian(double a[][], int index[]) {
        int n = index.length;
        double c[] = new double[n];

        // Initialize the index
        for (int i = 0; i < n; ++i)
            index[i] = i;

        // Find the rescaling factors, one from each row
        for (int i = 0; i < n; ++i) {
            double c1 = 0;
            for (int j = 0; j < n; ++j) {
                double c0 = Math.abs(a[i][j]);
                if (c0 > c1) c1 = c0;
            }
            c[i] = c1;
        }

        // Search the pivoting element from each column
        int k = 0;
        for (int j = 0; j < n - 1; ++j) {
            double pi1 = 0;
            for (int i = j; i < n; ++i) {
                double pi0 = Math.abs(a[index[i]][j]);
                pi0 /= c[index[i]];
                if (pi0 > pi1) {
                    pi1 = pi0;
                    k = i;
                }
            }

            // Interchange rows according to the pivoting order
            int itmp = index[j];
            index[j] = index[k];
            index[k] = itmp;
            for (int i = j + 1; i < n; ++i) {
                double pj = a[index[i]][j] / a[index[j]][j];

                // Record pivoting ratios below the diagonal
                a[index[i]][j] = pj;

                // Modify other elements accordingly
                for (int l = j + 1; l < n; ++l)
                    a[index[i]][l] -= pj * a[index[j]][l];
            }
        }
    }
}




