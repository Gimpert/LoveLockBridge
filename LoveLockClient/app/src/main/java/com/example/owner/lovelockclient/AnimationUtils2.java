package com.example.owner.lovelockclient;

/*
 * Copyright (C) 2007 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.content.Context;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.os.SystemClock;
import android.support.annotation.AnimRes;
import android.util.AttributeSet;
import android.util.Xml;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.AnticipateOvershootInterpolator;
import android.view.animation.BaseInterpolator;
import android.view.animation.BounceInterpolator;
import android.view.animation.CycleInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.GridLayoutAnimationController;
import android.view.animation.Interpolator;
import android.view.animation.LayoutAnimationController;
import android.view.animation.LinearInterpolator;
import android.view.animation.OvershootInterpolator;
import android.view.animation.PathInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

/**
 * Defines common utilities for working with animations.
 *
 */
public class AnimationUtils2 {

    /**
     * These flags are used when parsing AnimatorSet objects
     */
    private static final int TOGETHER = 0;
    private static final int SEQUENTIALLY = 1;


    /**
     * Returns the current animation time in milliseconds. This time should be used when invoking
     * {@link Animation#setStartTime(long)}. Refer to {@link android.os.SystemClock} for more
     * information about the different available clocks. The clock used by this method is
     * <em>not</em> the "wall" clock (it is not {@link System#currentTimeMillis}).
     *
     * @return the current animation time in milliseconds
     *
     * @see android.os.SystemClock
     */
    public static long currentAnimationTimeMillis() {
        return SystemClock.uptimeMillis();
    }

    /**
     * Loads an {@link Animation} object from a resource
     *
     * @param context Application context used to access resources
     * @param id The resource id of the animation to load
     * @return The animation object reference by the specified id
     * @throws Resources.NotFoundException when the animation cannot be loaded
     */
    public static Animation loadAnimation(Context context, @AnimRes int id)
            throws Resources.NotFoundException {

        XmlResourceParser parser = null;
//        try {
            parser = context.getResources().getAnimation(id);
            return new AlphaAnimation(context, Xml.asAttributeSet(parser));//createAnimationFromXml(context, parser);
//        } catch (XmlPullParserException ex) {
//            Resources.NotFoundException rnf = new Resources.NotFoundException("Can't load animation resource ID #0x" +
//                    Integer.toHexString(id));
//            rnf.initCause(ex);
//            throw rnf;
//        } catch (IOException ex) {
//            Resources.NotFoundException rnf = new Resources.NotFoundException("Can't load animation resource ID #0x" +
//                    Integer.toHexString(id));
//            rnf.initCause(ex);
//            throw rnf;
//        } finally {
//            if (parser != null) parser.close();
//        }
    }

    private static Animation createAnimationFromXml(Context c, XmlPullParser parser)
            throws XmlPullParserException, IOException {

        return createAnimationFromXml(c, parser, null, Xml.asAttributeSet(parser));
    }

    private static Animation createAnimationFromXml(Context c, XmlPullParser parser,
                                                    AnimationSet parent, AttributeSet attrs) throws XmlPullParserException, IOException {

        Animation anim = null;

        // Make sure we are on a start tag.
        int type;
        int depth = parser.getDepth();

        while (((type=parser.next()) != XmlPullParser.END_TAG || parser.getDepth() > depth)
                && type != XmlPullParser.END_DOCUMENT) {

            if (type != XmlPullParser.START_TAG) {
                continue;
            }

            String  name = parser.getName();

            if (name.equals("set")) {
                anim = new AnimationSet(c, attrs);
                createAnimationFromXml(c, parser, (AnimationSet)anim, attrs);
            } else if (name.equals("alpha")) {
                anim = new AlphaAnimation(c, attrs);
            } else if (name.equals("scale")) {
                anim = new ScaleAnimation(c, attrs);
            }  else if (name.equals("rotate")) {
                anim = new RotateAnimation(c, attrs);
            }  else if (name.equals("translate")) {
                anim = new TranslateAnimation(c, attrs);
            } else {
                throw new RuntimeException("Unknown animation name: " + parser.getName());
            }

            if (parent != null) {
                parent.addAnimation(anim);
                return anim;
            }
        }

        return anim;

    }

    /**
     * Loads a {@link LayoutAnimationController} object from a resource
     *
     * @param context Application context used to access resources
     * @param id The resource id of the animation to load
     * @return The animation object reference by the specified id
     * @throws Resources.NotFoundException when the layout animation controller cannot be loaded
     */
    public static LayoutAnimationController loadLayoutAnimation(Context context, @AnimRes int id)
            throws Resources.NotFoundException {

        XmlResourceParser parser = null;
        try {
            parser = context.getResources().getAnimation(id);
            return createLayoutAnimationFromXml(context, parser);
        } catch (XmlPullParserException ex) {
            Resources.NotFoundException rnf = new Resources.NotFoundException("Can't load animation resource ID #0x" +
                    Integer.toHexString(id));
            rnf.initCause(ex);
            throw rnf;
        } catch (IOException ex) {
            Resources.NotFoundException rnf = new Resources.NotFoundException("Can't load animation resource ID #0x" +
                    Integer.toHexString(id));
            rnf.initCause(ex);
            throw rnf;
        } finally {
            if (parser != null) parser.close();
        }
    }

    private static LayoutAnimationController createLayoutAnimationFromXml(Context c,
                                                                          XmlPullParser parser) throws XmlPullParserException, IOException {

        return createLayoutAnimationFromXml(c, parser, Xml.asAttributeSet(parser));
    }

    private static LayoutAnimationController createLayoutAnimationFromXml(Context c,
                                                                          XmlPullParser parser, AttributeSet attrs) throws XmlPullParserException, IOException {

        LayoutAnimationController controller = null;

        int type;
        int depth = parser.getDepth();

        while (((type = parser.next()) != XmlPullParser.END_TAG || parser.getDepth() > depth)
                && type != XmlPullParser.END_DOCUMENT) {

            if (type != XmlPullParser.START_TAG) {
                continue;
            }

            String name = parser.getName();

            if ("layoutAnimation".equals(name)) {
                controller = new LayoutAnimationController(c, attrs);
            } else if ("gridLayoutAnimation".equals(name)) {
                controller = new GridLayoutAnimationController(c, attrs);
            } else {
                throw new RuntimeException("Unknown layout animation name: " + name);
            }
        }

        return controller;
    }

    /**
     * Make an animation for objects becoming visible. Uses a slide and fade
     * effect.
     *
     * @param c Context for loading resources
     * @param fromLeft is the object to be animated coming from the left
     * @return The new animation
     */
//    public static Animation makeInAnimation(Context c, boolean fromLeft) {
//        Animation a;
//        if (fromLeft) {
//            a = AnimationUtils2.loadAnimation(c, com.android.internal.R.anim.slide_in_left);
//        } else {
//            a = AnimationUtils2.loadAnimation(c, com.android.internal.R.anim.slide_in_right);
//        }
//
//        a.setInterpolator(new DecelerateInterpolator());
//        a.setStartTime(currentAnimationTimeMillis());
//        return a;
//    }

    /**
     * Make an animation for objects becoming invisible. Uses a slide and fade
     * effect.
     *
     * @param c Context for loading resources
     * @param toRight is the object to be animated exiting to the right
     * @return The new animation
     */
//    public static Animation makeOutAnimation(Context c, boolean toRight) {
//        Animation a;
//        if (toRight) {
//            a = AnimationUtils2.loadAnimation(c, com.android.internal.R.anim.slide_out_right);
//        } else {
//            a = AnimationUtils2.loadAnimation(c, com.android.internal.R.anim.slide_out_left);
//        }
//
//        a.setInterpolator(new AccelerateInterpolator());
//        a.setStartTime(currentAnimationTimeMillis());
//        return a;
//    }


    /**
     * Make an animation for objects becoming visible. Uses a slide up and fade
     * effect.
     *
     * @param c Context for loading resources
     * @return The new animation
     */
//    public static Animation makeInChildBottomAnimation(Context c) {
//        Animation a;
//        a = AnimationUtils2.loadAnimation(c, com.android.internal.R.anim.slide_in_child_bottom);
//        a.setInterpolator(new AccelerateInterpolator());
//        a.setStartTime(currentAnimationTimeMillis());
//        return a;
//    }

    /**
     * Loads an {@link Interpolator} object from a resource
     *
     * @param context Application context used to access resources
     * @param id The resource id of the animation to load
     * @return The animation object reference by the specified id
     * @throws Resources.NotFoundException
     */
//    public static Interpolator loadInterpolator(Context context, @InterpolatorRes int id)
//            throws Resources.NotFoundException {
//        XmlResourceParser parser = null;
//        try {
//            parser = context.getResources().getAnimation(id);
//            return createInterpolatorFromXml(context.getResources(), context.getTheme(), parser);
//        } catch (XmlPullParserException ex) {
//            Resources.NotFoundException rnf = new Resources.NotFoundException("Can't load animation resource ID #0x" +
//                    Integer.toHexString(id));
//            rnf.initCause(ex);
//            throw rnf;
//        } catch (IOException ex) {
//            Resources.NotFoundException rnf = new Resources.NotFoundException("Can't load animation resource ID #0x" +
//                    Integer.toHexString(id));
//            rnf.initCause(ex);
//            throw rnf;
//        } finally {
//            if (parser != null) parser.close();
//        }
//
//    }

    /**
     * Loads an {@link Interpolator} object from a resource
     *
     * @param res The resources
     * @param id The resource id of the animation to load
     * @return The interpolator object reference by the specified id
     * @throws Resources.NotFoundException
     * @hide
     */
//    public static Interpolator loadInterpolator(Resources res, Resources.Theme theme, int id) throws Resources.NotFoundException {
//        XmlResourceParser parser = null;
//        try {
//            parser = res.getAnimation(id);
//            return createInterpolatorFromXml(res, theme, parser);
//        } catch (XmlPullParserException ex) {
//            Resources.NotFoundException rnf = new Resources.NotFoundException("Can't load animation resource ID #0x" +
//                    Integer.toHexString(id));
//            rnf.initCause(ex);
//            throw rnf;
//        } catch (IOException ex) {
//            Resources.NotFoundException rnf = new Resources.NotFoundException("Can't load animation resource ID #0x" +
//                    Integer.toHexString(id));
//            rnf.initCause(ex);
//            throw rnf;
//        } finally {
//            if (parser != null)
//                parser.close();
//        }
//
//    }

//    private static Interpolator createInterpolatorFromXml(Resources res, Resources.Theme theme, XmlPullParser parser)
//            throws XmlPullParserException, IOException {
//
//        BaseInterpolator interpolator = null;
//
//        // Make sure we are on a start tag.
//        int type;
//        int depth = parser.getDepth();
//
//        while (((type = parser.next()) != XmlPullParser.END_TAG || parser.getDepth() > depth)
//                && type != XmlPullParser.END_DOCUMENT) {
//
//            if (type != XmlPullParser.START_TAG) {
//                continue;
//            }
//
//            AttributeSet attrs = Xml.asAttributeSet(parser);
//
//            String name = parser.getName();
//
//            if (name.equals("linearInterpolator")) {
//                interpolator = new LinearInterpolator();
//            } else if (name.equals("accelerateInterpolator")) {
//                interpolator = new AccelerateInterpolator(res, theme, attrs);
//            } else if (name.equals("decelerateInterpolator")) {
//                interpolator = new DecelerateInterpolator(res, theme, attrs);
//            } else if (name.equals("accelerateDecelerateInterpolator")) {
//                interpolator = new AccelerateDecelerateInterpolator();
//            } else if (name.equals("cycleInterpolator")) {
//                interpolator = new CycleInterpolator(res, theme, attrs);
//            } else if (name.equals("anticipateInterpolator")) {
//                interpolator = new AnticipateInterpolator(res, theme, attrs);
//            } else if (name.equals("overshootInterpolator")) {
//                interpolator = new OvershootInterpolator(res, theme, attrs);
//            } else if (name.equals("anticipateOvershootInterpolator")) {
//                interpolator = new AnticipateOvershootInterpolator(res, theme, attrs);
//            } else if (name.equals("bounceInterpolator")) {
//                interpolator = new BounceInterpolator();
//            } else if (name.equals("pathInterpolator")) {
//                interpolator = new PathInterpolator(res, theme, attrs);
//            } else {
//                throw new RuntimeException("Unknown interpolator name: " + parser.getName());
//            }
//        }
//        return interpolator;
//    }
}
