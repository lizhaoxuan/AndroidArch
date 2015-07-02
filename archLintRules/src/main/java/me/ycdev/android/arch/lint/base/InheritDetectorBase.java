package me.ycdev.android.arch.lint.base;

import com.android.annotations.NonNull;
import com.android.annotations.Nullable;
import com.android.tools.lint.client.api.JavaParser;
import com.android.tools.lint.detector.api.Detector;
import com.android.tools.lint.detector.api.JavaContext;
import com.android.tools.lint.detector.api.Speed;

import java.util.HashSet;
import java.util.List;

import lombok.ast.ClassDeclaration;
import lombok.ast.Node;

public abstract class InheritDetectorBase extends Detector implements Detector.JavaScanner {
    protected HashSet<String> mWrapperClasses;

    /** Constructs a new {@link InheritDetectorBase} check */
    public InheritDetectorBase() {
        mWrapperClasses = getWrapperClasses();
    }

    protected abstract HashSet<String> getWrapperClasses();

    @Override
    public abstract List<String> applicableSuperClasses();

    protected abstract void reportViolation(JavaContext context, Node node);

    @Override
    public void checkClass(@NonNull JavaContext context, @Nullable ClassDeclaration declaration,
            @NonNull Node node, @NonNull JavaParser.ResolvedClass resolvedClass) {
        String superClassName = resolvedClass.getSuperClass().getName();
        if (!mWrapperClasses.contains(superClassName)) {
            reportViolation(context, node);
        }
    }

    @Override
    public Speed getSpeed() {
        return Speed.FAST;
    }
}
