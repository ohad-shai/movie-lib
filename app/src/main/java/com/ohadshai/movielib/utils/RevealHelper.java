package com.ohadshai.movielib.utils;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;

/**
 * Represents a helper for revealing layouts with the Circular Reveal Animation.
 * Created by Ohad on 3/17/2017.
 */
public class RevealHelper {

    //region Constants

    /**
     * Holds a constant for a reveal direction: "Center".
     */
    public static final int CENTER = 0x01;

    /**
     * Holds a constant for a reveal direction: "Center Vertical".
     */
    public static final int CENTER_VERTICAL = 0x02;

    /**
     * Holds a constant for a reveal direction: "Center Horizontal".
     */
    public static final int CENTER_HORIZONTAL = 0x04;

    /**
     * Holds a constant for a reveal direction: "Top".
     */
    public static final int TOP = 0x08;

    /**
     * Holds a constant for a reveal direction: "Bottom".
     */
    public static final int BOTTOM = 0x16;

    /**
     * Holds a constant for a reveal direction: "Left".
     */
    public static final int LEFT = 0x32;

    /**
     * Holds a constant for a reveal direction: "Right".
     */
    public static final int RIGHT = 0x64;

    //endregion

    //region Private Members

    /**
     * Holds an indicator indicating whether the layout is revealed or not.
     */
    private boolean _isRevealed;

    /**
     * Holds an indicator indicating whether currently restoring the revealed state or not.
     */
    private boolean _isRestoring;

    /**
     * Holds an indicator indicating whether currently animating the reveal.
     */
    private boolean _isAnimatingReveal;

    /**
     * Holds the activity owner.
     */
    private Activity _activity;

    /**
     * Holds the container layout to reveal / conceal.
     */
    private View _layout;

    /**
     * Holds the key identifies the layout to reveal.
     */
    private String _key;

    /**
     * Holds the callback methods for the reveal helper.
     */
    private RevealHelperCallback _callbacks;

    //region [ Reveal From ]

    /**
     * Holds the X position to reveal the layout from.
     */
    private int _revealX = 0;

    /**
     * Holds the Y position to reveal the layout from.
     */
    private int _revealY = 0;

    //endregion

    //region [ Conceal To ]

    /**
     * Holds an indicator indicating whether a "conceal to" values are provided or not.
     */
    private boolean _isConcealToProvided = false;

    /**
     * Holds the X position to reveal the layout from.
     */
    private int _concealX = 0;

    /**
     * Holds the Y position to reveal the layout from.
     */
    private int _concealY = 0;

    //endregion

    //region [ Status Bar Transition ]

    /**
     * Holds an indicator if the status bar transition is enabled or not.
     */
    private boolean _isStatusBarTransitionEnabled = false;

    /**
     * Holds the color of the status bar when the layout is concealed.
     */
    private int _statusFromColor;

    /**
     * Holds the color of the status bar when the layout is revealed.
     */
    private int _statusToColor;

    /**
     * Holds the duration of the status bar reveal transition.
     */
    private int _statusRevealTransitionDuration;

    /**
     * Holds the duration of the status bar reveal delay.
     */
    private int _statusRevealDelay;

    /**
     * Holds the duration of the status bar conceal transition.
     */
    private int _statusConcealTransitionDuration;

    /**
     * Holds the duration of the status bar conceal delay.
     */
    private int _statusConcealDelay;

    /**
     * Holds the status bar transition interpolator.
     */
    private TimeInterpolator _statusInterpolator;

    //endregion

    //region [ Reveal Radius ]

    /**
     * Holds an indicator indicating whether a reveal radius is provided or not.
     */
    private boolean _isRevealRadiusProvided = false;

    /**
     * Holds the radius the reveal will be started with.
     */
    private float _revealStartRadius;

    /**
     * Holds the radius the reveal will be ended with.
     */
    private float _revealEndRadius;

    //endregion

    //region [ Conceal Radius ]

    /**
     * Holds an indicator indicating whether a conceal radius is provided or not.
     */
    private boolean _isConcealRadiusProvided = false;

    /**
     * Holds the radius the conceal will be started with.
     */
    private float _concealStartRadius;

    /**
     * Holds the radius the conceal will be ended with.
     */
    private float _concealEndRadius;

    //endregion

    //region [ Reveal Interpolator ]

    /**
     * Holds the reveal animation interpolator.
     */
    private TimeInterpolator _revealInterpolator;

    //endregion

    //region [ Conceal Interpolator ]

    /**
     * Holds the conceal animation interpolator.
     */
    private TimeInterpolator _concealInterpolator;

    //endregion

    //endregion

    //region C'tor

    /**
     * Initializes a new instance of a helper for revealing layouts with the Circular Reveal Animation.
     *
     * @param activity  The activity owner.
     * @param layout    The layout container to reveal.
     * @param key       The key identifies the layout to reveal.
     * @param callbacks The callback methods of the reveal helper.
     */
    private RevealHelper(@NonNull Activity activity, @NonNull View layout, @NonNull String key, @NonNull RevealHelperCallback callbacks) {
        if (key.trim().equals(""))
            throw new IllegalArgumentException("key is empty.");

        this._activity = activity;
        this._layout = layout;
        this._key = key;
        this._callbacks = callbacks;

        this._layout.setVisibility(View.INVISIBLE);
    }

    //endregion

    //region Events

    /**
     * Handles a back press in the helper owner, to close the state of the reveal helper.
     *
     * @return Returns true if the back press was handled by the helper, otherwise false.
     */
    public boolean onBackPress() {
        if (_isRevealed) {
            this.conceal();
            return true;
        } else {
            return false;
        }
    }

    /**
     * Saves the state of the reveal helper, to the owner's outState.
     *
     * @param outState The {@link Bundle} outState of the reveal helper owner, to save the state in.
     */
    public void onSaveInstanceState(Bundle outState) {
        if (outState == null)
            return;

        // Saves the indicator indicating whether the state is revealed or not:
        outState.putBoolean(_key, _isRevealed);
    }

    /**
     * Restores the state of the reveal helper, by the owner's savedInstanceState.
     *
     * @param savedInstanceState The {@link Bundle} savedInstanceState of the reveal helper owner, to get the state from.
     */
    public void onRestoreInstanceState(@Nullable Bundle savedInstanceState) {
        if (savedInstanceState == null)
            return;

        _isRestoring = true;

        // Gets the indicator indicating whether the state is revealed or not:
        _isRevealed = savedInstanceState.getBoolean(_key, false);

        // Restores the state:
        if (_isRevealed)
            this.show();

        _isRestoring = false;
    }

    //endregion

    //region Public Static API

    /**
     * Creates a new instance of a helper for revealing layouts with the Circular Reveal Animation.
     *
     * @param activity  The activity owner.
     * @param layout    The layout container to reveal.
     * @param key       The key identifies the layout to reveal.
     * @param callbacks The callback methods of the reveal helper.
     * @return Returns the new instance created of the reveal helper.
     */
    public static RevealHelper create(@NonNull Activity activity, @NonNull View layout, @NonNull String key, @NonNull RevealHelperCallback callbacks) {
        return new RevealHelper(activity, layout, key, callbacks);
    }

    //endregion

    //region Public API

    //region [ Create Methods Related ]

    /**
     * Sets from where to reveal the layout.
     *
     * @param revealFrom The view to reveal the layout from.
     * @return Returns this instance of the reveal helper.
     */
    public RevealHelper setRevealFrom(@NonNull View revealFrom) {
        // Gets the location of the view on the screen:
        int[] location = new int[2];
        revealFrom.getLocationOnScreen(location);

        this._revealX = location[0] + (revealFrom.getWidth() / 2);
        this._revealY = revealFrom.getHeight() / 2;
        return this;
    }

    /**
     * Sets the X and Y positions where to reveal the layout.
     *
     * @param revealX The X position to reveal the layout from.
     * @param revealY The Y position to reveal the layout from.
     * @return Returns this instance of the reveal helper.
     */
    public RevealHelper setRevealFrom(int revealX, int revealY) {
        this._revealX = revealX;
        this._revealY = revealY;
        return this;
    }

    /**
     * Sets from where to reveal the layout.
     *
     * @param direction The direction to reveal the layout from.
     * @return Returns this instance of the reveal helper.
     */
    public RevealHelper setRevealFrom(int direction) {
        if ((direction & RevealHelper.CENTER) == RevealHelper.CENTER) {
            this._revealX = _layout.getWidth() / 2;
            this._revealY = _layout.getHeight() / 2;
        }

        if ((direction & RevealHelper.CENTER_VERTICAL) == RevealHelper.CENTER_VERTICAL) {
            this._revealY = _layout.getHeight() / 2;
        }

        if ((direction & RevealHelper.CENTER_HORIZONTAL) == RevealHelper.CENTER_HORIZONTAL) {
            this._revealX = _layout.getWidth() / 2;
        }

        if ((direction & RevealHelper.TOP) == RevealHelper.TOP) {
            this._revealY = 0;
        }

        if ((direction & RevealHelper.BOTTOM) == RevealHelper.BOTTOM) {
            this._revealY = _layout.getHeight();
        }

        if ((direction & RevealHelper.LEFT) == RevealHelper.LEFT) {
            this._revealX = 0;
        }

        if ((direction & RevealHelper.RIGHT) == RevealHelper.RIGHT) {
            this._revealX = _layout.getWidth();
        }

        return this;
    }

    /**
     * Sets the start radius and the end radius of the reveal animation.
     *
     * @param startRadius The start radius of the reveal.
     * @param endRadius   The end radius of the reveal.
     * @return Returns this instance of the reveal helper.
     */
    public RevealHelper setRevealRadius(float startRadius, float endRadius) {
        this._isRevealRadiusProvided = true;

        this._revealStartRadius = startRadius;
        this._revealEndRadius = endRadius;
        return this;
    }

    /**
     * Sets the X and Y positions to conceal the layout.
     *
     * @param concealX The X position to conceal the layout to.
     * @param concealY The Y position to conceal the layout to.
     * @return Returns this instance of the reveal helper.
     */
    public RevealHelper setConcealTo(int concealX, int concealY) {
        this._isConcealToProvided = true;

        this._concealX = concealX;
        this._concealY = concealY;
        return this;
    }

    /**
     * Sets the start radius and the end radius of the conceal animation.
     *
     * @param startRadius The start radius of the conceal.
     * @param endRadius   The end radius of the conceal.
     * @return Returns this instance of the reveal helper.
     */
    public RevealHelper setConcealRadius(float startRadius, float endRadius) {
        this._isConcealRadiusProvided = true;

        this._concealStartRadius = startRadius;
        this._concealEndRadius = endRadius;
        return this;
    }

    /**
     * Sets a reveal animation interpolator.
     *
     * @param interpolator The reveal interpolator to set.
     * @return Returns this instance of the reveal helper.
     */
    public RevealHelper setRevealInterpolator(TimeInterpolator interpolator) {
        this._revealInterpolator = interpolator;
        return this;
    }

    /**
     * Sets a conceal animation interpolator.
     *
     * @param interpolator The conceal interpolator to set.
     * @return Returns this instance of the reveal helper.
     */
    public RevealHelper setConcealInterpolator(TimeInterpolator interpolator) {
        this._concealInterpolator = interpolator;
        return this;
    }

    /**
     * Sets the status bar transition.
     *
     * @param fromColor       The color of the status bar when the layout is concealed.
     * @param toColor         The color of the status bar when the layout is revealed.
     * @param revealDuration  The duration of the status bar reveal transition.
     * @param concealDuration The duration of the status bar conceal transition.
     * @return Returns this instance of the reveal helper.
     */
    public RevealHelper setStatusBarTransition(int fromColor, int toColor, int revealDuration, int concealDuration) {
        return this.setStatusBarTransition(fromColor, toColor, revealDuration, 0, concealDuration, 0, null);
    }

    /**
     * Sets the status bar transition.
     *
     * @param fromColor       The color of the status bar when the layout is concealed.
     * @param toColor         The color of the status bar when the layout is revealed.
     * @param revealDuration  The duration of the status bar reveal transition.
     * @param revealDelay     The duration of the status bar reveal delay.
     * @param concealDuration The duration of the status bar conceal transition.
     * @param concealDelay    The duration of the status bar conceal delay.
     * @return Returns this instance of the reveal helper.
     */
    public RevealHelper setStatusBarTransition(int fromColor, int toColor, int revealDuration, int revealDelay, int concealDuration, int concealDelay) {
        return this.setStatusBarTransition(fromColor, toColor, revealDuration, revealDelay, concealDuration, concealDelay, null);
    }

    /**
     * Sets the status bar transition.
     *
     * @param fromColor       The color of the status bar when the layout is concealed.
     * @param toColor         The color of the status bar when the layout is revealed.
     * @param revealDuration  The duration of the status bar reveal transition.
     * @param revealDelay     The duration of the status bar reveal delay.
     * @param concealDuration The duration of the status bar conceal transition.
     * @param concealDelay    The duration of the status bar conceal delay.
     * @param interpolator    The status bar transition interpolator.
     * @return Returns this instance of the reveal helper.
     */
    public RevealHelper setStatusBarTransition(int fromColor, int toColor, int revealDuration, int revealDelay, int concealDuration, int concealDelay, TimeInterpolator interpolator) {
        _isStatusBarTransitionEnabled = true;

        this._statusFromColor = fromColor;
        this._statusToColor = toColor;
        this._statusRevealTransitionDuration = revealDuration;
        this._statusRevealDelay = revealDelay;
        this._statusConcealTransitionDuration = concealDuration;
        this._statusConcealDelay = concealDelay;
        this._statusInterpolator = interpolator;
        return this;
    }

    //endregion

    /**
     * Indicates whether the layout is revealed or not.
     *
     * @return Returns true if the layout is revealed, otherwise false.
     */
    public boolean isRevealed() {
        return _isRevealed;
    }

    /**
     * Shows the layout with no animation (if already showing - does nothing).
     */
    public void show() {
        if (_isRevealed && !_isRestoring)
            return;

        if (_callbacks != null) {
            _callbacks.onReveal();
            _callbacks.initLayoutControls();
        }

        _layout.setVisibility(View.VISIBLE);
        _isRevealed = true;

        if (!_isAnimatingReveal && _callbacks != null)
            _callbacks.onRevealed();

        // Checks if the reveal helper state is restoring:
        if (_isRestoring) {
            if (_isStatusBarTransitionEnabled) {
                // Works only for API 21+
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    Window window = _activity.getWindow();
                    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                    window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                    window.setStatusBarColor(_statusToColor);
                }
            }
        }
    }

    /**
     * Hides the layout with no animation (if not showing - does nothing).
     */
    public void hide() {
        if (!_isRevealed)
            return;

        if (_callbacks != null)
            _callbacks.onConceal();

        _isRevealed = false;
        _layout.setVisibility(View.INVISIBLE);

        if (_callbacks != null)
            _callbacks.onConcealed();
    }

    /**
     * Toggles the layout to reveal / conceal.
     */
    public void toggle() {
        if (_isRevealed)
            this.conceal();
        else
            this.reveal();
    }

    /**
     * Reveals the layout (if already revealed - does nothing).
     */
    public void reveal() {
        if (_isRevealed && !_isRestoring)
            return;

        _isAnimatingReveal = true;

        if (_callbacks != null)
            _callbacks.onReveal();

        this.show();

        //region Reveal Animation...

        // Checks if to set the default reveal radius:
        if (!_isRevealRadiusProvided) {
            this._revealStartRadius = 0;
            this._revealEndRadius = (float) Math.hypot(_layout.getWidth(), _layout.getHeight());
        }

        Animator anim = null;
        // Checks and targets the animation capabilities:
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // Circular Reveal for API above 21:
            anim = ViewAnimationUtils.createCircularReveal(_layout, _revealX, _revealY, _revealStartRadius, _revealEndRadius);
            anim.setInterpolator(new AccelerateInterpolator());
        } else {
            // Fade In for API below 21:
            _layout.setAlpha(0);
            anim = ObjectAnimator.ofFloat(_layout, "alpha", 1);
        }

        if (_revealInterpolator != null)
            anim.setInterpolator(_revealInterpolator);

        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                _isAnimatingReveal = false;

                if (_callbacks != null)
                    _callbacks.onRevealed();
            }
        });
        anim.start();

        //endregion

        //region Status Bar Animation...

        if (_isStatusBarTransitionEnabled) {
            // Works only for API 21+
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                final Window window = _activity.getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

                ValueAnimator colorAnimation = ValueAnimator.ofArgb(_statusFromColor, _statusToColor);
                colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animator) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            if (_isRevealed)
                                window.setStatusBarColor((Integer) animator.getAnimatedValue());
                        }
                    }
                });
                colorAnimation.setDuration(_statusRevealTransitionDuration);
                colorAnimation.setStartDelay(_statusRevealDelay);
                colorAnimation.start();
            }
        }

        //endregion
    }

    /**
     * Conceals the layout (if not revealed - does nothing).
     */
    public void conceal() {
        if (!_isRevealed)
            return;

        if (_callbacks != null)
            _callbacks.onConceal();

        _isRevealed = false;

        //region Conceal Animation...

        // Checks if to set the default conceal X and Y positions (the same as the reveal positions):
        if (!_isConcealToProvided) {
            this._concealX = this._revealX;
            this._concealY = this._revealY;
        }
        // Checks if to set the default conceal radius (layout width & height):
        if (!_isConcealRadiusProvided) {
            this._concealStartRadius = (float) Math.hypot(_layout.getWidth(), _layout.getHeight());
            this._concealEndRadius = 0;
        }

        Animator anim = null;
        // Checks and targets the animation capabilities:
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // Circular Reveal for API above 21:
            anim = ViewAnimationUtils.createCircularReveal(_layout, _concealX, _concealY, _concealStartRadius, _concealEndRadius);
            anim.setInterpolator(new DecelerateInterpolator());
        } else {
            // Fade In for API below 21:
            _layout.setAlpha(1);
            anim = ObjectAnimator.ofFloat(_layout, "alpha", 0);
        }

        if (_concealInterpolator != null)
            anim.setInterpolator(_concealInterpolator);

        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                _layout.setVisibility(View.INVISIBLE);

                if (_callbacks != null)
                    _callbacks.onConcealed();
            }
        });
        anim.start();

        //endregion

        //region Status Bar Animation...

        if (_isStatusBarTransitionEnabled) {
            // Works only for API 21+
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                final Window window = _activity.getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

                ValueAnimator colorAnimation = ValueAnimator.ofArgb(_statusToColor, _statusFromColor);
                colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animator) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                            window.setStatusBarColor((Integer) animator.getAnimatedValue());
                    }
                });
                colorAnimation.setDuration(_statusConcealTransitionDuration);
                colorAnimation.setStartDelay(_statusConcealDelay);
                colorAnimation.start();
            }
        }

        //endregion
    }

    //endregion

    //region Inner Classes

    /**
     * Represents the callbacks for the reveal helper.
     */
    public static abstract class RevealHelperCallback implements RevealHelperRequiredCallback {

        /**
         * Event occurs before revealing the layout.
         */
        public void onReveal() {
        }

        /**
         * Event occurs after the layout is revealed.
         */
        public void onRevealed() {
        }

        /**
         * Event occurs before concealing the layout.
         */
        public void onConceal() {
        }

        /**
         * Event occurs after the layout is concealed.
         */
        public void onConcealed() {
        }

    }

    /**
     * Represents the required callbacks for the reveal helper.
     */
    static interface RevealHelperRequiredCallback {

        /**
         * Event occurs when the controls in the layout needs to be initialized.
         */
        void initLayoutControls();

    }

    //endregion

}
