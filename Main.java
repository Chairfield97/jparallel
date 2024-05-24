import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.ConsoleErrorListener;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class Main {
    public static void main(String[] args) {

        FileWriter file = null;
        PrintWriter out = null;

        for (int i = 0; i < args.length; i++) {
            if (!args[i].contains(".java")) {
                continue;
            }
            JavaLexer lexer = null;
            try {
                lexer = new JavaLexer(CharStreams.fromFileName(args[i]));
            } catch (Exception e) {
                System.out.println("Could not open file '" + args[i] + "' for reading.");
                return;
            }
            CommonTokenStream tokens = new CommonTokenStream(lexer);
            JavaParser parser = new JavaParser(tokens);
            ParseTree tree = parser.compilationUnit();
            if (tree == null) {
                System.out.println("Could not parse.");
            }

            try {
                file = new FileWriter("out.java");
                out = new PrintWriter(file);

            } catch (IOException e) {
                System.out.println("An error occurred.");
                e.printStackTrace();
            }

            JavaParserBaseVisitor visitors [] = {
                    new HelloWorldVisitor(out)
            };

            for (JavaParserBaseVisitor visitor: visitors) {
                try {
                    visitor.visit(tree);
                } catch(Exception e) {
                    System.out.println("error visiting.");
                }
            }
        }
        try {
            out.close();
            file.close();
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

    }
}

