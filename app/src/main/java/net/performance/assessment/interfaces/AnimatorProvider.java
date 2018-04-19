package net.performance.assessment.interfaces;

import android.animation.Animator;
import android.view.View;

/**
 *
 */

public interface AnimatorProvider
{
    Animator showAnimation(View view );

    Animator hideAnimation(View view);
}
