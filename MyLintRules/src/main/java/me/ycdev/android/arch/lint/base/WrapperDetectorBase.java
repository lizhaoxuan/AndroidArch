package me.ycdev.android.arch.lint.base;

import com.android.tools.lint.client.api.JavaParser;
import com.android.tools.lint.detector.api.Detector;
import com.android.tools.lint.detector.api.JavaContext;

import java.util.List;

import lombok.ast.AstVisitor;
import lombok.ast.ClassDeclaration;
import lombok.ast.MethodInvocation;

public abstract class WrapperDetectorBase extends Detector implements Detector.JavaScanner {
    protected String mWrapperClassName;
    protected String[] mTargetClassFullNames;

    /** Constructs a new {@link WrapperDetectorBase} check */
    public WrapperDetectorBase() {
        mWrapperClassName = getWrapperClassName();
        mTargetClassFullNames = getTargetClassFullNames();
    }

    // ---- Implements JavaScanner ----

    protected abstract String getWrapperClassName();

    protected abstract String[] getTargetClassFullNames();

    protected abstract void reportViolation(JavaContext context, MethodInvocation node);

    @Override
    public abstract List<String> getApplicableMethodNames();

    @Override
    public void visitMethod(JavaContext context, AstVisitor visitor, MethodInvocation node) {
        if (node.astOperand() == null) {
            return;
        }

        String operand = node.astOperand().toString();
        if (operand.endsWith(mWrapperClassName)) {
            return;
        }

        if (isInvokedInWrapperClass(node)) {
            return;
        }

        if (checkRuleViolation(context, node)) {
            reportViolation(context, node);
        }
    }

    private boolean checkRuleViolation(JavaContext context, MethodInvocation node) {
        JavaParser.ResolvedNode resolved = context.resolve(node);
        if (resolved instanceof JavaParser.ResolvedMethod) {
            JavaParser.ResolvedMethod method = (JavaParser.ResolvedMethod) resolved;
            JavaParser.ResolvedClass containingClass = method.getContainingClass();
            String fullClassName = containingClass.getName();
            for (String targetClassName : mTargetClassFullNames) {
                if (targetClassName.equals(fullClassName)) {
                    return true;
                }
                if (containingClass.isSubclassOf(targetClassName, false)) {
                    return true;
                }
            }
            return false;
        }
        return false;
    }

    private boolean isInvokedInWrapperClass(MethodInvocation node) {
        ClassDeclaration surroundingClass = JavaContext.findSurroundingClass(node);
        String className = surroundingClass.astName().astValue();
        return mWrapperClassName.equals(className);
    }
}
