package emojireaction;

/*
import android.content.res.TypedArray;
import android.graphics.drawable.ColorDrawable;
import android.view.MotionEvent;
import android.view.animation.AccelerateInterpolator;
import static android.util.TypedValue.TYPE_DIMENSION;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.Rect;
import android.widget.ImageView;
*/

import ohos.agp.animation.Animator;
import ohos.agp.animation.Animator.StateChangedListener;
import ohos.agp.animation.AnimatorValue;
import ohos.agp.components.*;
import ohos.agp.render.*;
import ohos.agp.utils.Color;
import ohos.agp.utils.Matrix;
import ohos.agp.utils.Rect;
import ohos.agp.utils.RectFloat;
import ohos.app.Context;
import ohos.global.resource.NotExistException;
import ohos.global.resource.WrongTypeException;
import ohos.media.image.PixelMap;
import ohos.media.image.common.Size;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import static ohos.agp.animation.Animator.CurveType.ACCELERATE;
import static ohos.agp.render.BlendMode.MULTIPLY;
import static ohos.media.image.common.PixelFormat.ARGB_8888;


/**
 * This class does all the background work related to displaying emoji on the canvas
 */
public class EmojiReactionView extends Image implements Component.DrawTask {
    // Click interface for emoji click
    private emojireaction.ClickInterface mClickInterface;
    // Array of emoji PixelMaps
    private PixelMap[] emojiPixelMap;
    // Index of the selected PixelMap
    private int clickedEmojiNumber = -1;
    // Total number of emoji PixelMaps
    private int numberOfEmojis = 0;
    // ArrayList containing all emoji ids to be displayed
    private ArrayList<Integer> emojiId = new ArrayList<>();
    // Activity context
    private Context context;
    // Density factor
    private final float densityFactor = context.getResourceManager().getDeviceCapability().screenDensity;
            //getResources().getDisplayMetrics().density;
    // Variable for getting the dimension height or width, whichever is smaller
    private int smallerDimension;
    // boolean to declare if a touch gesture was a swipe
    private boolean wasSwiping = false;
    // boolean to declare if a touch gesture was a swipe and started from an emoji on panel
    private boolean emojiClicked = false;

    /// Home emoji variables

    // Home emoji rect variable
    private Rect homeRect = new Rect();
    // Length of side of home rect
    private int homeSide = (int) (50 * densityFactor);
    // Home emoji center
    private int[] homeCenter = new int[]{(int) (30 * densityFactor), 0};
    // Raw home emoji center Y coordinate given by the user
    private float homeCenterYGiven = -1;
    // Home emoji PixelMap
    private PixelMap homePixelMap;
    // Boolean to make home emoji visible
    private boolean homeEmojiVisible;

    //TODO: can be improved in landscape mode!!
    /// Circular Animation Variables

    // Panel center
    private int[] panelCentre = new int[2];
    // Raw panel center given by user
    private float[] panelCentreGiven = new float[]{-2, -2};
    // Panel radius variable
    private int panelRadius;
    // Raw panel radius variable given by user
    private int panelRadiusGiven = -1;
    // Emoji icon side length
    private int panelEmojiSide = (int) (50 * densityFactor);
    // Coordinates for emoji translation
    private int[][] emojiMovingPoint;
    // Variable used to measure the length of a path, and/or to find the position and tangent along it
    private PathMeasure[] pms;
    // Boolean checks whether animation is working or not
    private boolean panelAnimWorking;
    // Matrix is used emojis that are present in circular reveal as there is a rotating animation for them
    private Matrix[] emojiMatrix;
    int iCurStep = 20;// current step
    int iCurStepTaken = 0;// current steps total

    /// Variables for clicking and un-clicking gesture detection

    // Radius for defining clicked region radius
    private int clickedRadius;
    // Paint object for creating a white background around the emoji when clicked
    private Paint clickedPaint = new Paint();
    // Boolean to check that click animation is working or not
    private boolean clickingAnimWorking;

    /// RisingEmoji

    // speed per frame of the rising emojis
    private int emojisRisingSpeed = (int) (10 * densityFactor);
    // height of the rising emojis(to start disappearing)
    private int emojisRisingHeight;
    // raw height of the rising emojis(to start disappearing) given by user
    private float emojisRisingHeightGiven = -2;
    // Arraylist storing properties of rising emojis
    private ArrayList<com.example.emojireactionview.RisingEmoji> risingEmojis = new ArrayList<>();
    // Total number of emojis rising
    private int numberOfRisers = 24;
    // Boolean to check if emojis are rising
    private boolean emojiRising;
    // Timer to rise emojis continuously
    private Timer emojiRisingTimer;
    // Variable to count number of emojis started disappearing
    private int fading = 0;
    // is the emojis started disappearing
    private boolean startFading = false;


    public EmojiReactionView(Context context) throws NotExistException, WrongTypeException, IOException {
        super(context);
        init();
    }

    public EmojiReactionView(Context context, AttrSet attrs) throws NotExistException, WrongTypeException, IOException {
        this(context, attrs, 0);
    }

    public EmojiReactionView(Context context, AttrSet attrs, int defStyle) throws NotExistException, WrongTypeException, IOException {
        super(context, attrs, String.valueOf(defStyle));
        this.context = context;
        // extract data from attributes
        this.initBaseXMLAttrs(context, attrs);
        init();
    }

    final void initBaseXMLAttrs(Context context, AttrSet attrs) {

        /*
        final TypedArray arr = context.obtainStyledAttributes(attrs, R.styleable.EmojiReactionView);
        final int N = arr.getIndexCount();
        for (int i = 0; i < N; ++i) {
            int attr = arr.getIndex(i);

            if (attr == R.styleable.EmojiReactionView_emojis) {
                final TypedArray resourceArray = context.getResources().obtainTypedArray(arr.getResourceId(R.styleable.EmojiReactionView_emojis, 0));
                // Get emojis id from attribute and store it in arraylist
                for (int j = 0; j < resourceArray.length(); j++) {
                    emojiId.add(resourceArray.getResourceId(j, 0));
                }
                numberOfEmojis = emojiId.size();
                resourceArray.recycle();

            } else if (attr == R.styleable.EmojiReactionView_set_emoji) {
                clickedEmojiNumber = arr.getInt(attr, clickedEmojiNumber);
            } else if (attr == R.styleable.EmojiReactionView_home_Center_X) {
                homeCenter[0] = arr.getDimensionPixelSize(attr, homeCenter[0]);

            } else if (attr == R.styleable.EmojiReactionView_home_Center_Y) {
                homeCenterYGiven = arr.getDimensionPixelSize(attr, -1);

            } else if (attr == R.styleable.EmojiReactionView_home_side) {
                homeSide = arr.getDimensionPixelSize(attr, homeSide);

            } else if (attr == R.styleable.EmojiReactionView_panel_center_X) {
                if (arr.peekValue(attr).type == TYPE_DIMENSION)
                    panelCentreGiven[0] = arr.getDimensionPixelSize(attr, -2);
                else {
                    panelCentreGiven[0] = checkFraction(arr.getFraction(attr, -1, -1, -2));
                }
            } else if (attr == R.styleable.EmojiReactionView_panel_center_Y) {
                if (arr.peekValue(attr).type == TYPE_DIMENSION)
                    panelCentreGiven[1] = arr.getDimensionPixelSize(attr, -2);
                else
                    panelCentreGiven[1] = checkFraction(arr.getFraction(attr, -1, -1, -2));
            } else if (attr == R.styleable.EmojiReactionView_panel_radius) {
                panelRadiusGiven = arr.getDimensionPixelSize(attr, -1);

            } else if (attr == R.styleable.EmojiReactionView_panel_emoji_side) {
                panelEmojiSide = arr.getDimensionPixelSize(attr, panelEmojiSide);

            } else if (attr == R.styleable.EmojiReactionView_emojis_rising_height) {
                emojisRisingHeightGiven = checkFraction(arr.getFraction(attr, -1, -1, -2));
            } else if (attr == R.styleable.EmojiReactionView_emojis_rising_speed) {
                emojisRisingSpeed = arr.getDimensionPixelSize(attr, -1);

            } else if (attr == R.styleable.EmojiReactionView_emojis_rising_number) {
                numberOfRisers = arr.getInt(attr, numberOfRisers);
            }
        }
        if (clickedEmojiNumber >= numberOfEmojis || clickedEmojiNumber < -1) {
            throw new IllegalArgumentException("set_emoji can't be more than number of emojis!");
        }
         arr.recycle();
        */

       // final TypedArray arr = context.obtainStyledAttributes(attrs, R.styleable.EmojiReactionView);

        final int N = attrs.getLength();
        for (int i = 0; i < N; ++i) {
            Attr attr = attrs.getAttr(i).get();

            if (attr == R.styleable.EmojiReactionView_emojis) {
                final TypedArray resourceArray = context.getResources().obtainTypedArray(
                        arr.getResourceId(R.styleable.EmojiReactionView_emojis, 0));
                // Get emojis id from attribute and store it in arraylist
                for (int j = 0; j < resourceArray.length(); j++) {
                    emojiId.add(resourceArray.getResourceId(j, 0));
                }
                numberOfEmojis = emojiId.size();
                resourceArray.recycle();

            //} else if (attr == R.styleable.EmojiReactionView_set_emoji) {
            //    clickedEmojiNumber = arr.getInt(attr, clickedEmojiNumber);
            } else if (attr.getIntegerValue() != 0) {
                clickedEmojiNumber = attr.getIntegerValue();

            //} else if (attr == R.styleable.EmojiReactionView_home_Center_X) {
            //    homeCenter[0] = arr.getDimensionPixelSize(attr, homeCenter[0]);
            } else if (attr == R.styleable.EmojiReactionView_home_Center_X) {
                homeCenter[0] = arr.getDimensionPixelSize(attr, homeCenter[0]);

            } else if (attr == R.styleable.EmojiReactionView_home_Center_Y) {
                homeCenterYGiven = arr.getDimensionPixelSize(attr, -1);

            } else if (attr == R.styleable.EmojiReactionView_home_side) {
                homeSide = arr.getDimensionPixelSize(attr, homeSide);

            } else if (attr == R.styleable.EmojiReactionView_panel_center_X) {
                if (arr.peekValue(attr).type == TYPE_DIMENSION)
                    panelCentreGiven[0] = arr.getDimensionPixelSize(attr, -2);
                else {
                    panelCentreGiven[0] = checkFraction(arr.getFraction(attr, -1, -1, -2));
                }
            } else if (attr == R.styleable.EmojiReactionView_panel_center_Y) {
                if (arr.peekValue(attr).type == TYPE_DIMENSION)
                    panelCentreGiven[1] = arr.getDimensionPixelSize(attr, -2);
                else
                    panelCentreGiven[1] = checkFraction(arr.getFraction(attr, -1, -1, -2));
            } else if (attr == R.styleable.EmojiReactionView_panel_radius) {
                panelRadiusGiven = arr.getDimensionPixelSize(attr, -1);

            } else if (attr == R.styleable.EmojiReactionView_panel_emoji_side) {
                panelEmojiSide = arr.getDimensionPixelSize(attr, panelEmojiSide);

            } else if (attr == R.styleable.EmojiReactionView_emojis_rising_height) {
                emojisRisingHeightGiven = checkFraction(arr.getFraction(attr, -1, -1, -2));
            } else if (attr == R.styleable.EmojiReactionView_emojis_rising_speed) {
                emojisRisingSpeed = arr.getDimensionPixelSize(attr, -1);

            } else if (attr == R.styleable.EmojiReactionView_emojis_rising_number) {
                numberOfRisers = arr.getInt(attr, numberOfRisers);
            }
        }
        if (clickedEmojiNumber >= numberOfEmojis || clickedEmojiNumber < -1) {
            throw new IllegalArgumentException("set_emoji can't be more than number of emojis!");
        }





        numberOfEmojis = emojiId.size();
        emojiPixelMap = new PixelMap[numberOfEmojis];

    }

    private float checkFraction(float input) {
        // Check that percents entered is within [0% 100%]
        if (input == -2 || (input >= -1 && input <= 0)) {
            return input;
        } else throw new IllegalArgumentException();
    }

    public int[] getCentre() {
        return panelCentre;
    }

    public float getRadius() {
        return panelRadius;
    }

    public int getClickedEmojiNumber() {
        return clickedEmojiNumber;
    }

    public void setClickedEmojiNumber(int clickedEmojiNumber) throws NotExistException, WrongTypeException, IOException {
        // Set the currently selected emoji with immediate effect
        if (mClickInterface != null)

            if (clickedEmojiNumber == -1) {
                mClickInterface.onEmojiUnclicked(clickedEmojiNumber, -1, -1);
            } else if (this.clickedEmojiNumber == clickedEmojiNumber) {
                mClickInterface.onEmojiUnclicked(this.clickedEmojiNumber, -1, -1);

            } else if (clickedEmojiNumber >= 0 && clickedEmojiNumber < numberOfEmojis) {
                mClickInterface.onEmojiUnclicked(this.clickedEmojiNumber, -1, -1);
                mClickInterface.onEmojiClicked(clickedEmojiNumber, -1, -1);
            } else throw new IllegalArgumentException("clickedEmojiNumber out of range");

        this.clickedEmojiNumber = clickedEmojiNumber;
        // Get the Emoji to be displayed in PixelMap
        if(emojiPixelMap == null) emojiPixelMap = new PixelMap[numberOfEmojis];
        if (clickedEmojiNumber != -1 && emojiPixelMap[clickedEmojiNumber] == null)
            emojiPixelMap[clickedEmojiNumber] = getPixelMapFromId(emojiId.get(clickedEmojiNumber), panelEmojiSide);
        this.invalidate();
    }

    public int getNumberOfEmojis() {
        return numberOfEmojis;
    }

    public int getEmojisRisingSpeed() {
        return emojisRisingSpeed;
    }

    public Rect getHomeRect() {
        return homeRect;
    }

    public int getPanelEmojiSide() {
        return panelEmojiSide;
    }

    public PixelMap getHomePixelMap() {
        return homePixelMap;
    }

    public void setHomePixelMap(PixelMap homePixelMap) {
        this.homePixelMap = homePixelMap;
    }

    public boolean isHomeEmojiVisible() {
        return homeEmojiVisible;
    }

    public boolean isPanelAnimWorking() {
        return panelAnimWorking;
    }

    public boolean isClickingAnimWorking() {
        return clickingAnimWorking;
    }

    public boolean isEmojiRising() {
        return emojiRising;
    }

    public void setHomeEmojiVisible() {
        // Make the home emoji visible
        if (emojiRising) {
            emojiRising = false;
            if (emojiRisingTimer != null) {
                emojiRisingTimer.cancel();
                emojiRisingTimer.purge();
                emojiRisingTimer = null;
            }
            fading = 0;
            startFading = false;
            if (risingEmojis.size() != 0) risingEmojis.clear();
        }
        homeEmojiVisible = true;
        panelAnimWorking = false;
        clickingAnimWorking = false;
        ColorFilter colorFilter = new ColorFilter(Color.rgb(255, 255, 255), MULTIPLY);
        clickedPaint.setColorFilter(colorFilter);

       // setColorFilter(Color.rgb(255, 255, 255), android.graphics.PorterDuff.Mode.MULTIPLY);
    }

    public void setPanelAnimWorking() {
        // Start Circular animation

        if (emojiMatrix == null) return;

        if (emojiRising) {
            emojiRising = false;
            if (emojiRisingTimer != null) {
                emojiRisingTimer.cancel();
                emojiRisingTimer.purge();
                emojiRisingTimer = null;
            }
            fading = 0;
            startFading = false;
            if (risingEmojis.size() != 0) risingEmojis.clear();
        }

        panelAnimWorking = true;
        homeEmojiVisible = false;
        clickingAnimWorking = false;
        invalidate();
    }

    public void setOnEmojiClickListener(ClickInterface l) {
        // Set the listener to the clickedEmojiNumber
        this.mClickInterface = l;
    }

    private void init() throws NotExistException, WrongTypeException, IOException {
        addDrawTask(this);
        // The paint which dims the background on circular animation
        Color c = new Color(Color.argb(150, 185, 185, 185));
        clickedPaint.setColor(c);
        // Get the Emoji to be displayed in PixelMap
        if (clickedEmojiNumber != -1) {
            emojiPixelMap[clickedEmojiNumber] = getPixelMapFromId(emojiId.get(clickedEmojiNumber), panelEmojiSide);
        }

        setup();
    }

    private void setup() {

        if (emojiId == null) {
            return;
        }
        if (numberOfEmojis == 0) {
            homeEmojiVisible = false;
            panelAnimWorking = false;
            clickingAnimWorking = false;
            emojiRising = false;
            return;
        }

        if (getWidth() == 0 && getHeight() == 0) {
            return;
        } else {
            smallerDimension = getHeight() > getWidth() ? getWidth() : getHeight();
        }

        // set emojisRisingHeight based on user data
        if (emojisRisingHeightGiven == -2) {
            emojisRisingHeight = getHeight() / 2;
        } else if (emojisRisingHeightGiven <= 0 && emojisRisingHeightGiven >= -1) {
            emojisRisingHeight = getHeight() - (int) (emojisRisingHeightGiven * getHeight());
        }
        setHomeRect();
        setPathPanel();

        homeEmojiVisible = true;
    }

    private void setHomeRect() {
        // Set the homeRect y coordinate based on user data
        if (homeCenterYGiven == -1) {
            homeCenter[1] = getHeight() - (int) (30 * densityFactor);
        } else {
            homeCenter[1] = (int) (getHeight() - homeCenterYGiven);
        }

        // Set the homePixelMap with the default PixelMap
        // use R.drawable.home to access or modify it
        if (homePixelMap == null)
            homePixelMap = getPixelMapFromId(R.drawable.home, homeSide);
        // Set the home Rect
        homeRect = new Rect((homeCenter[0] - homeSide / 2), (homeCenter[1] - homeSide / 2), (homeCenter[0] + homeSide / 2), (homeCenter[1] + homeSide / 2));
    }

    private void setPathPanel() {
        // Set the coordinates for circular animation
        if (panelCentreGiven[0] == -2)
            panelCentre[0] = getWidth() / 2;
        else if (panelCentreGiven[0] >= -1 && panelCentreGiven[0] <= 0) {
            panelCentre[0] = 0 - (int) (panelCentreGiven[0] * getWidth());
        } else if (panelCentreGiven[0] >= 0) {
            panelCentre[0] = (int) (panelCentreGiven[0]);
        }
        if (panelCentreGiven[1] == -2)
            panelCentre[1] = getHeight() - panelEmojiSide / 2;
        else if (panelCentreGiven[1] >= -1 && panelCentreGiven[1] <= 0) {
            panelCentre[1] = getHeight() + (int) (panelCentreGiven[1] * getHeight());
        } else if (panelCentreGiven[1] >= 0) {
            panelCentre[1] = getHeight() - (int) (panelCentreGiven[1]);
        }
        //TODO: need to look somewhat flatter

        // Set the radius of circular animation
        if (panelRadiusGiven == -1) {
            panelRadius = (int) (smallerDimension / 2 - 20 * densityFactor);
        } else {
            panelRadius = smallerDimension / 2 > panelRadiusGiven ? panelRadiusGiven : smallerDimension / 2;
        }

        clickedRadius = (int) (panelEmojiSide * 0.65);
        // angle between successive emojis
        double angle = Math.PI / (numberOfEmojis + 1);

        // set up PathMeasure object between initial and final coordinates
        Path emojiPath1;
        pms = new PathMeasure[numberOfEmojis];
        for (int i = 1; i <= numberOfEmojis; i++) {
            emojiPath1 = new Path();
            emojiPath1.moveTo(panelCentre[0], panelCentre[1]);
            emojiPath1.lineTo((float) (panelCentre[0] + panelRadius * Math.cos(i * angle + Math.PI)), (float) (panelCentre[1] + panelRadius * Math.sin(i * angle + Math.PI)));
            pms[i - 1] = new PathMeasure(emojiPath1, false);
        }

    }
    @Override
    public void addDrawTask(DrawTask task){
        super.addDrawTask(task);

    }

    @Override
    public void onDraw(Component component, Canvas canvas) {
        //super.onDraw(canvas);
        if (homeEmojiVisible)

            // To make home emoji visible
            if (clickedEmojiNumber == -1){
                PixelMapHolder pixelMapHolder = new PixelMapHolder(homePixelMap);
                canvas.drawPixelMapHolderRect(pixelMapHolder, new RectFloat(homeRect),null);
            }
            else{
                PixelMapHolder pixelMapHolder = new PixelMapHolder(emojiPixelMap[clickedEmojiNumber]);
                canvas.drawPixelMapHolderRect(pixelMapHolder, new RectFloat(homeRect),null);
            }

        if (panelAnimWorking) {
            // to start circular animation
            // to display clicked circle on clicked emoji
            if (clickedEmojiNumber != -1) {
                canvas.drawCircle(emojiMovingPoint[clickedEmojiNumber][0], emojiMovingPoint[clickedEmojiNumber][1], clickedRadius, clickedPaint);
            }
            for (int i = 0; i < numberOfEmojis; i++){
                PixelMapHolder pixelMapHolder = new PixelMapHolder(emojiPixelMap[i]);
                canvas.drawPixelMapHolderRect(pixelMapHolder, new RectFloat(emojiMatrix[i]), null);
                //canvas.drawBitmap(emojiBitmap[i], emojiMatrix[i], null);
            }
            try {
                startPanelAnim();
            } catch (NotExistException e) {
                e.printStackTrace();
            } catch (WrongTypeException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (clickingAnimWorking) {
            // to start clicking animation
            // Display clicked circle
            if (clickedEmojiNumber != -1) {
                canvas.drawCircle(emojiMovingPoint[clickedEmojiNumber][0], emojiMovingPoint[clickedEmojiNumber][1], clickedRadius, clickedPaint);
            }
            for (int i = 0; i < numberOfEmojis; i++) {
                PixelMapHolder pixelMapHolder= new PixelMapHolder(emojiPixelMap[i]);
                canvas.drawPixelMapHolderRect(pixelMapHolder, new RectFloat(emojiMatrix[i]), null);
                //canvas.drawBitmap(emojiBitmap[i], emojiMatrix[i], null);
            }
        }

        if (emojiRising) {
            // display rising emojis at their new position
            for (com.example.emojireactionview.RisingEmoji re : risingEmojis) {
                PixelMapHolder pixelMapHolder= new PixelMapHolder(emojiPixelMap[clickedEmojiNumber]);
                canvas.drawPixelMapHolderRect(pixelMapHolder,new RectFloat(re.getRect()), re.getPaint());
                //canvas.drawBitmap(emojiBitmap[clickedEmojiNumber], null, re.getRect(), re.getPaint());

            }
        }
    }

    private void startPanelAnim() throws NotExistException, WrongTypeException, IOException {
        // start circular animation

        // float array to store new coordinates
        float[] emojiMovingPointFloat = new float[2];

        // initialise matrix array
        if (emojiMatrix == null) {
            emojiMatrix = new Matrix[numberOfEmojis];
        }

        // get emojiPixelMap from their ids
        for (int i = 0; i < numberOfEmojis; i++) {
            if (emojiPixelMap[i] == null) {
                emojiPixelMap[i] = getPixelMapFromId(emojiId.get(i), panelEmojiSide);
            }
        }

        // initialise array to hold emoji coordinates
        if (emojiMovingPoint == null) {
            emojiMovingPoint = new int[numberOfEmojis][2];
            for (int i = 0; i < numberOfEmojis; i++) {
                emojiMovingPoint[i] = new int[2];
            }
        }

        float fSegmentLen = pms[0].getLength() / 210;//210 animation steps

        if (iCurStep >= 0) {
            for (int i = 0; i < numberOfEmojis; i++) {
                pms[i].getPosTan(fSegmentLen * iCurStepTaken, emojiMovingPointFloat, null);
                emojiMovingPoint[i] = convertFloatArrayToIntArray(emojiMovingPointFloat, emojiMovingPoint[i]);
                emojiMatrix[i] = new Matrix();
//                int scale = panelEmojiSide / emojiPixelMap[0].getHeight();
//                emojiMatrix.get(i).setScale(scale, scale);
                // move PixelMap to the coordinate
                emojiMatrix[i].postTranslate(emojiMovingPoint[i][0] - panelEmojiSide / 2, emojiMovingPoint[i][1] - panelEmojiSide / 2);
                // rotate it with required effect
                emojiMatrix[i].postRotate((iCurStep) * (75 - 150 * (i + 1) / (numberOfEmojis + 1)) / 20, emojiMovingPoint[i][0], emojiMovingPoint[i][1]);
            }
            // to get deceleration effect
            iCurStepTaken += iCurStep;
            iCurStep--;
            // gradually dim background
            ColorFilter colorFilter = new ColorFilter(Color.rgb(255 - 6 * (20 - iCurStep), 255 - 6 * (20 - iCurStep), 255 - 6 * (20 - iCurStep)), MULTIPLY);
            clickedPaint.setColorFilter(colorFilter);
          //  setColorFilter(Color.rgb(255 - 6 * (20 - iCurStep), 255 - 6 * (20 - iCurStep), 255 - 6 * (20 - iCurStep)), android.graphics.PorterDuff.Mode.MULTIPLY);
        } else {
            iCurStep = 20;
            iCurStepTaken = 0;
        }
    }

    private int[] convertFloatArrayToIntArray(float[] emojiMovingPointFloat, int[] ints) {
        ints[0] = (int) emojiMovingPointFloat[0];
        ints[1] = (int) emojiMovingPointFloat[1];
        return ints;
    }

    private void startClickingAnim(final int clickedIndex) {
        // start clicking animation

        panelAnimWorking = false;
        clickingAnimWorking = true;
        AnimatorValue animatorValue = new AnimatorValue();
        animatorValue.setDelay(1);
        animatorValue.setDuration((long)0.4f);
               // AnimatorValue.ofFloat(1, 0.4f);
        animatorValue.setValueUpdateListener(new AnimatorValue.ValueUpdateListener() {
            @Override
            public void onUpdate(AnimatorValue animatorValue, float v) {
                emojiMatrix[clickedIndex].setScale((float) animatorValue.getAnimatedValue(), (float) animatorValue.getAnimatedValue());
                emojiMatrix[clickedIndex].postTranslate(emojiMovingPoint[clickedIndex][0] - (float) animatorValue.getAnimatedValue() * panelEmojiSide / 2, emojiMovingPoint[clickedIndex][1] - (float) animatorValue.getAnimatedValue() * panelEmojiSide / 2);
                invalidate();
            }
        });

        animatorValue.setStateChangedListener(new StateChangedListener() {
            @Override
            public void onStart(Animator animator) {

            }

            @Override
            public void onStop(Animator animator) {

            }

            @Override
            public void onCancel(Animator animator) {

            }

            @Override
            public void onEnd(Animator animator) {
                // restore background dimness

                ColorFilter colorFilter = new ColorFilter(Color.rgb(255, 255, 255), MULTIPLY);
                clickedPaint.setColorFilter(colorFilter);
              //  setColorFilter(Color.rgb(255, 255, 255), android.graphics.PorterDuff.Mode.MULTIPLY);
                clickingAnimWorking = false;
                clickedEmojiNumber = clickedIndex;

                // start emoji rising animation
                emojiRisinginit();

            }

            @Override
            public void onPause(Animator animator) {

            }

            @Override
            public void onResume(Animator animator) {

            }
        });

        /*
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {

            }
        });
         */

        animatorValue.setDuration(500);
        animatorValue.setCurveType(ACCELERATE);
        animatorValue.start();
    }

    public static AnimatorValue ofFloat(float start, float end) {
        AnimatorValue animatorValue = new AnimatorValue();
        animatorValue.setDelay((long) start);
        animatorValue.setDuration((long) end);
        return animatorValue;

    }

    private void startUnclickingAnim(final int clickedIndex) {
        // start unclicking animation

        panelAnimWorking = false;
        clickingAnimWorking = true;
        AnimatorValue animatorValue = new AnimatorValue();
        //AnimatorValue.ofFloat(1, 0.4f);
        animatorValue.setDelay(1);
        animatorValue.setDuration((long) 0.4f);


        animatorValue.setValueUpdateListener(new AnimatorValue.ValueUpdateListener() {
            @Override
            public void onUpdate(AnimatorValue animatorValue, float v) {
                emojiMatrix[clickedIndex].setScale((float) animatorValue.getAnimatedValue(), (float) animatorValue.getAnimatedValue());
                emojiMatrix[clickedIndex].postTranslate(emojiMovingPoint[clickedIndex][0] - (float) animatorValue.getAnimatedValue() * panelEmojiSide / 2, emojiMovingPoint[clickedIndex][1] - (float) animatorValue.getAnimatedValue() * panelEmojiSide / 2);

                // reduce the size of background panel radius
                clickedRadius = (int) (panelEmojiSide * (float) animatorValue.getAnimatedValue() * 0.65);
                invalidate();
            }
        });
        animatorValue.setStateChangedListener(new StateChangedListener() {
            @Override
            public void onStart(Animator animator) {

            }

            @Override
            public void onStop(Animator animator) {

            }

            @Override
            public void onCancel(Animator animator) {

            }

            @Override
            public void onEnd(Animator animator) {
                clickingAnimWorking = false;
                clickedEmojiNumber = -1;
                homeEmojiVisible = true;
                // restore background dimness
                ColorFilter colorFilter = new ColorFilter(Color.rgb(255, 255, 255),MULTIPLY);
                clickedPaint.setColorFilter(colorFilter);

                //setColorFilter(Color.rgb(255, 255, 255), android.graphics.PorterDuff.Mode.MULTIPLY);
                clickedRadius = (int) (panelEmojiSide * 0.65);
            }

            @Override
            public void onPause(Animator animator) {

            }

            @Override
            public void onResume(Animator animator) {

            }
        });
        /*
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                clickingAnimWorking = false;
                clickedEmojiNumber = -1;
                homeEmojiVisible = true;
                // restore background dimness
                setColorFilter(Color.rgb(255, 255, 255), android.graphics.PorterDuff.Mode.MULTIPLY);
                clickedRadius = (int) (panelEmojiSide * 0.65);
            }
        });

         */
        animatorValue.setDuration(500);
        animatorValue.setCurveType(ACCELERATE);
        //animator.setInterpolator(new AccelerateInterpolator());
        animatorValue.start();
    }

    private void emojiRisinginit() {
        // set up rising emojis

        // directly invalidate if numberOfRisers<=0
        if (numberOfRisers <= 0) {
            homeEmojiVisible = true;
            invalidate();
            return;
        }

        emojiRising = true;

        //set up emojis with random behaviour at random position
        for (int i = 0; i < numberOfRisers; i++) {
            Rect risingRect = calculateNewRect(new Rect(), new Random().nextInt(
                    getWidth() + 1), getHeight() + new Random().nextInt(2 * getHeight() / 5), new Random().nextInt(panelEmojiSide / 4) + panelEmojiSide / 3);
            risingEmojis.add(new com.example.emojireactionview.RisingEmoji(risingRect, new Random().nextInt(6) + emojisRisingSpeed));
        }
        // start animation
        emojiRisingAnim();
    }

    private void emojiRisingAnim() {
        // timer to continuously rise emojis

        if (emojiRisingTimer == null) {
            emojiRisingTimer = new Timer();
            emojiRisingTimer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    // calculate new coordinates in non ui thread
                    riseEmoji();
                    invalidate();
                    //((Ability) context).findComponentById().in
                    /*
                    ((Ability) context).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            // display the emojis at their new position
                            invalidate();
                        }
                    });
                     */
                }
            }, 5, 40);
        }

    }

    private void riseEmoji() {
        // calculate new coordinates

        if (startFading)
            fading += new Random().nextInt(numberOfRisers / 5);
        // currently index to which emojis will be faded
        for (int i = 0; i < risingEmojis.size(); i++) {
            com.example.emojireactionview.RisingEmoji re = risingEmojis.get(i);
            // update rects to their new positions
            re.setRect(calculateNewRect(re.getRect(), re.getRect().getCenterX(), re.getRect().getCenterY() - re.getSpeed(), re.getRect().getWidth()/ 2));

            //if any of the emoji crossed the threshold height, start fading emojis
            if (startFading || re.getRect().top <= emojisRisingHeight) {
                startFading = true;
                if (fading > risingEmojis.size()) fading = risingEmojis.size();
                if (re.getPaint() == null) re.setPaint(new Paint());

                // if emojis index is smaller than currently fading index
                if (i <= fading) {
                    // fade
                    re.getPaint().setAlpha(re.getPaint().getAlpha() / 2);
                    // increase size
                    re.setRect(calculateNewRect(re.getRect(), re.getRect().getCenterX(), re.getRect().getCenterY(), (int) (re.getRect().getWidth() * 0.55)));
                }

                // remove if size almost completely faded
                if (re.getPaint().getAlpha() < 10) {
                    risingEmojis.remove(risingEmojis.get(i));
                    i--;
                }

            }

            // stop animation if every emoji has faded
            if (risingEmojis.size() == 0) {
                emojiRising = false;
                if (emojiRisingTimer != null) {
                    emojiRisingTimer.cancel();
                    emojiRisingTimer.purge();
                    emojiRisingTimer = null;
                }
                homeEmojiVisible = true;
                fading = 0;
                startFading = false;
                break;
            }
        }
    }
    //TODO set emoji before anim starts
    //TODO hover over emojis effect

    @SuppressWarnings("ClickableViewAccessibility")
   // @Override

    public boolean onTouchEvent(DragEvent event) throws NotExistException, WrongTypeException, IOException {
        // respond to touch events

        if (event.getAction() == DragEvent.DRAG_DROP) {
            // prepare for new gesture
            wasSwiping = false;
            emojiClicked = false;
            if (homeEmojiVisible && homeRect.isInclude((int) event.getX(), (int) event.getY())){
                emojiClicked = true;
            } else if (panelAnimWorking) {
                for (int i = numberOfEmojis - 1; i >= 0; i--) {
                    if (clickedOnEmojiReact(i, event.getX(), event.getY())) {
                        emojiClicked = true;
                        break;
                    }
                }
                if (clickedEmojiNumber != -1 && clickedOnRing(event.getX(), event.getY(), clickedEmojiNumber)) {
                    emojiClicked = true;
                }
            }
            super.onDrag(this, event);
            return true;
        } else if (!wasSwiping && event.getAction() == DragEvent.DRAG_MOVE) {
            // swiping gesture detected
            wasSwiping = true;

        } else if (event.getAction() == DragEvent.DRAG_MOVE || event.getAction() == DragEvent.DRAG_FINISH) {
            if (wasSwiping && !emojiClicked) {
                // gesture ends with homeEmoji visible
                panelAnimWorking = false;
                homeEmojiVisible = true;

                ColorFilter colorFilter = new ColorFilter(Color.rgb(255, 255, 255),MULTIPLY);
                clickedPaint.setColorFilter(colorFilter);


               // setColorFilter(Color.rgb(255, 255, 255), android.graphics.PorterDuff.Mode.MULTIPLY);
            } else {
                // time to respond to clicks on emojiRect
                if (panelAnimWorking) {
                    // detect if clicked on EmojiReact
                    for (int i = numberOfEmojis - 1; i >= 0; i--) {
                        if (clickedOnEmojiReact(i, event.getX(), event.getY())) {

                            if (clickedEmojiNumber == i) {
                                if (mClickInterface != null)
                                    mClickInterface.onEmojiUnclicked(i, (int) event.getX(), (int) event.getY());
                                startUnclickingAnim(clickedEmojiNumber);
                                return false;
                            } else if (clickedEmojiNumber != -1) {
                                if (mClickInterface != null)
                                    mClickInterface.onEmojiUnclicked(clickedEmojiNumber, (int) event.getX(), (int) event.getY());
                            }

                            if (mClickInterface != null)
                                mClickInterface.onEmojiClicked(i, (int) event.getX(), (int) event.getY());
                            startClickingAnim(i);
                            return false;
                        }
                    }
                    // detect if clicked on circle surrounding clicked Emoji
                    if (clickedEmojiNumber != -1 && clickedOnRing(event.getX(), event.getY(), clickedEmojiNumber)) {
                        if (mClickInterface != null)
                            mClickInterface.onEmojiUnclicked(clickedEmojiNumber, (int) event.getX(), (int) event.getY());
                        startUnclickingAnim(clickedEmojiNumber);
                        return false;
                    }
                    if (!wasSwiping) {
                        // clicked on background

                        // restore background dimness
                        panelAnimWorking = false;
                        homeEmojiVisible = true;
                        ColorFilter colorFilter = new ColorFilter(Color.rgb(255, 255, 255), MULTIPLY);
                        clickedPaint.setColorFilter(colorFilter);
                       // setColorFilter(Color.rgb(255, 255, 255), android.graphics.PorterDuff.Mode.MULTIPLY);
                    }
                } else if (homeEmojiVisible && homeRect.isInclude((int) event.getX(), (int) event.getY())){
                    // detect if clicked on homeEmoji
                    homeEmojiVisible = false;
                    panelAnimWorking = true;
                    startPanelAnim();
                    return false;
                }
                return super.onDrag(this,event);

            }
        }
        return super.onDrag(this,event);

    }

    private boolean clickedOnEmojiReact(int i, double x, double y) {
        return (Math.abs(x - emojiMovingPoint[i][0]) < panelEmojiSide / 2 && Math.abs(y - emojiMovingPoint[i][1]) < panelEmojiSide / 2);
    }

    private boolean clickedOnRing(float x, float y, int clickedEmojiNumber) {
        return (Math.pow(x - emojiMovingPoint[clickedEmojiNumber][0], 2) + Math.pow(y - emojiMovingPoint[clickedEmojiNumber][1], 2) <= Math.pow(clickedRadius, 2));
    }

    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        //super.onSizeChanged(w, h, oldw, oldh);
        setup();
    }

    @Override
    public void setPadding(int left, int top, int right, int bottom) {
        super.setPadding(left, top, right, bottom);
        setup();
    }

    @Override
    public void setPaddingRelative(int start, int top, int end, int bottom) {
        super.setPaddingRelative(start, top, end, bottom);
        setup();
    }

    private PixelMap getPixelMapFromId(int id, int side)throws NotExistException, WrongTypeException, IOException {
        // generate PixelMap from id of defined size
        if (side <= 0)
            throw new IllegalArgumentException("Emoji can't have 0 or negative side");
        ohos.agp.components.element.Element element = getImageElement();
       // Element element = context.getResourceManager().getElement(id);

        if (element == null) {return null;}
        try {
            PixelMap pixelMap;
            /*
            if (drawable instanceof ColorDrawable) {
                 pixelMap =  PixelMap.create(ARGB_8888,2,2);
            } else {
                pixelMap = PixelMap.create(ARGB_8888,side,side);
            }
             */
            PixelMap.InitializationOptions op = new PixelMap.InitializationOptions();
            op.pixelFormat = ARGB_8888;
            op.size = new Size(side,side);
            pixelMap = PixelMap.create(op);

            PixelMapHolder pixelMapHolder = new PixelMapHolder(pixelMap);
            element.setBounds(0, 0, side, side);
            Canvas c = new Canvas();
            c.drawPixelMapHolder(pixelMapHolder,0,0,null);
            element.drawToCanvas(c);
            return pixelMap;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private Rect calculateNewRect(Rect initialRect, int x, int y, int halfSide){
        // return rect with center x,y and side of length 2*halfside
        initialRect.left = x - halfSide;
        initialRect.right = x + halfSide;
        initialRect.top = y - halfSide;
        initialRect.bottom = y + halfSide;
        return initialRect;
    }

}
