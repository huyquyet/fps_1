package framgia.vn.photoSketch.bitmaputil;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RadialGradient;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.media.ExifInterface;
import android.media.ThumbnailUtils;
import android.os.Environment;
import android.util.TypedValue;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import framgia.vn.photoSketch.constants.ConstActivity;

/**
 * Created by nghicv on 14/04/2016.
 */
public class BitmapUtil {
    public static final int BITMAP_SIZE = 960;
    public static final float SEPIA_RED = 1f;
    public static final float SEPIA_BLUE = 0.4f;
    public static final float SEPIA_GREEN = 0.6f;
    public static final float HUE_VALUE = 180f;
    public static final float HUE_BRIGHTNESS = 100f;
    public static final float HUE_CONTRAST = 127f;
    public static final String FOLDER_NAME = "Photo";
    public static final String FILE_NAME = "image_";
    public static final String IMAGE_TYPE = ".png";
    public static final float ORIENTATION_ROTATE_90 = 90;

    public static int dpToPx(float dp, Resources res) {
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, res.getDisplayMetrics());
        return (int) px;
    }

    public static Bitmap resize(String imageUrl, int reqWith, int reqHeight) throws IOException {
        BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
        bitmapOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imageUrl, bitmapOptions);
        int inSampleSize = 1;
        final int outWidth = bitmapOptions.outWidth;
        final int outHeight = bitmapOptions.outHeight;
        if (outHeight > reqHeight || outWidth > reqWith) {
            final int halfWidth = outWidth / 2;
            final int halfHeight = outHeight / 2;
            while ((halfHeight / inSampleSize) > reqHeight && (halfWidth / inSampleSize) > reqWith) {
                inSampleSize *= 2;
            }
        }
        bitmapOptions.inSampleSize = inSampleSize;
        bitmapOptions.inJustDecodeBounds = false;
        return BitmapUtil.modifyOrientation(BitmapFactory.decodeFile(imageUrl, bitmapOptions), imageUrl);
    }

    public static Bitmap reSizeImage(Bitmap bitmap, float maxSize, boolean filter) {
        if (maxSize > bitmap.getWidth() && maxSize > bitmap.getHeight()) {
            float ratio = Math.min((float) maxSize / bitmap.getWidth(), (float) maxSize / bitmap.getHeight());
            int width = Math.round(ratio * bitmap.getWidth());
            int height = Math.round(ratio * bitmap.getHeight());
            return Bitmap.createScaledBitmap(bitmap, width, height, filter);
        }
        return bitmap;
    }

    public static Bitmap createThumbnailBitmap(Context context, Bitmap bitmap, int width, int height) {
        return ThumbnailUtils.extractThumbnail(bitmap, dpToPx(width, context.getResources()),
                dpToPx(height, context.getResources()));
    }

    public static Bitmap highlight(Bitmap bitmap, int scale) {
        Bitmap outBitmap = Bitmap.createBitmap(bitmap.getWidth() + scale, bitmap.getHeight() + scale,
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(outBitmap);
        canvas.drawColor(0, PorterDuff.Mode.CLEAR);
        Paint paintBlur = new Paint();
        paintBlur.setMaskFilter(new BlurMaskFilter(15, BlurMaskFilter.Blur.NORMAL));
        int[] offsetXY = new int[2];
        Bitmap bmAlpha = bitmap.extractAlpha(paintBlur, offsetXY);
        Paint paintAlphaColor = new Paint();
        paintAlphaColor.setColor(0xFFFFFFFF);
        canvas.drawBitmap(bmAlpha, offsetXY[0], offsetXY[1], paintAlphaColor);
        bmAlpha.recycle();
        canvas.drawBitmap(bitmap, 0, 0, null);
        return outBitmap;
    }

    public static Bitmap invert(Bitmap bitmap) {
        int height = bitmap.getHeight();
        int width = bitmap.getWidth();
        ColorMatrix colorMatrix_Inverted = new ColorMatrix(new float[]{
                -1, 0, 0, 0, 255,
                0, -1, 0, 0, 255,
                0, 0, -1, 0, 255,
                0, 0, 0, 1, 0});

        ColorFilter ColorFilter_Sepia = new ColorMatrixColorFilter(colorMatrix_Inverted);
        Bitmap bitmapOut = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmapOut);
        Paint paint = new Paint();
        paint.setColorFilter(ColorFilter_Sepia);
        canvas.drawBitmap(bitmap, 0, 0, paint);
        return bitmapOut;
    }

    /**
     * @param bitmap
     * @param scale  0 -> 1
     * @return
     */
    public static Bitmap greyScale(Bitmap bitmap, float scale) {
        int width, height;
        height = bitmap.getHeight();
        width = bitmap.getWidth();

        Bitmap bmpGrayscale = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bmpGrayscale);
        Paint paint = new Paint();
        ColorMatrix cm = new ColorMatrix();
        cm.setSaturation(scale);
        ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
        paint.setColorFilter(f);
        c.drawBitmap(bitmap, 0, 0, paint);
        return bmpGrayscale;
    }

    public static Bitmap rotate(Bitmap bitmap, float scale) {
        Matrix matrix = new Matrix();
        matrix.postRotate(scale);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    /**
     * @param bitmap Image bitmap
     * @param scale  -100 -> 100
     * @return Image bitmap
     */
    public static Bitmap brightness(Bitmap bitmap, int scale) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        Bitmap bmpBrightness = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bmpBrightness);
        Paint paint = new Paint();
        ColorMatrix cm = new ColorMatrix();
        float value = Math.min(HUE_BRIGHTNESS, Math.max(-HUE_BRIGHTNESS, scale));
        float[] mat = new float[]
                {
                        1, 0, 0, 0, value,
                        0, 1, 0, 0, value,
                        0, 0, 1, 0, value,
                        0, 0, 0, 1, 0,
                        0, 0, 0, 0, 1
                };
        cm.postConcat(new ColorMatrix(mat));
        ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
        paint.setColorFilter(f);
        c.drawBitmap(bitmap, 0, 0, paint);
        return bmpBrightness;
    }

    /**
     * @param source Image bitmap
     * @param scale  -255 -> 255
     * @return Image bitmap
     */
    public static Bitmap hue(Bitmap source, int scale) {
        int width = source.getWidth();
        int height = source.getHeight();
        Bitmap bmpHue = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bmpHue);
        Paint paint = new Paint();
        ColorMatrix cm = new ColorMatrix();
        float value = Math.min(HUE_VALUE, Math.max(-HUE_VALUE, scale)) / HUE_VALUE * (float) Math.PI;
        float cosVal = (float) Math.cos(value);
        float sinVal = (float) Math.sin(value);
        float lumR = 0.213f;
        float lumG = 0.715f;
        float lumB = 0.072f;
        float[] mat = new float[]
                {
                        lumR + cosVal * (1 - lumR) + sinVal * (-lumR), lumG + cosVal * (-lumG) + sinVal * (-lumG), lumB + cosVal * (-lumB) + sinVal * (1 - lumB), 0, 0,    // red
                        lumR + cosVal * (-lumR) + sinVal * (0.143f), lumG + cosVal * (1 - lumG) + sinVal * (0.140f), lumB + cosVal * (-lumB) + sinVal * (-0.283f), 0, 0,    // green
                        lumR + cosVal * (-lumR) + sinVal * (-(1 - lumR)), lumG + cosVal * (-lumG) + sinVal * (lumG), lumB + cosVal * (1 - lumB) + sinVal * (lumB), 0, 0,    // blue
                        0f, 0f, 0f, 1f, 0f, // alpha
                        0f, 0f, 0f, 0f, 1f
                };
        cm.postConcat(new ColorMatrix(mat));
        ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
        paint.setColorFilter(f);
        c.drawBitmap(source, 0, 0, paint);
        return bmpHue;
    }

    /**
     * @param bitmap
     * @param scale  0 ->1
     * @return
     */
    public static Bitmap sepia(Bitmap bitmap, float scale) {
        int width, height;
        height = bitmap.getHeight();
        width = bitmap.getWidth();

        Bitmap bitmapOut = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bitmapOut);
        Paint paint = new Paint();
        final ColorMatrix colorMatrix = new ColorMatrix();
        // applying scales for RGB color values
        colorMatrix.setScale(SEPIA_RED, SEPIA_GREEN, SEPIA_BLUE, scale);  //setScale(r,g,b,a);
        ColorFilter colorFilter = new ColorMatrixColorFilter(colorMatrix);
        paint.setColorFilter(colorFilter);
        c.drawBitmap(bitmap, 0, 0, paint);
        return bitmapOut;
    }

    public static Bitmap vignette(Bitmap image) {
        final int width = image.getWidth();
        final int height = image.getHeight();
        float radius = (float) (width / 1.2);
        int[] colors = new int[]{0, 0x55000000, 0xff000000};
        float[] positions = new float[]{0.0f, 0.5f, 2f};
        RadialGradient gradient = new RadialGradient(width / 2, height / 2, radius, colors, positions, Shader.TileMode.CLAMP);
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawARGB(1, 0, 0, 0);
        final Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(Color.BLACK);
        paint.setShader(gradient);
        final Rect rect = new Rect(0, 0, image.getWidth(), image.getHeight());
        final RectF rectf = new RectF(rect);
        canvas.drawRect(rectf, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(image, rect, rect, paint);
        return bitmap;
    }

    public static final Bitmap sketch(Bitmap bitmap) {
        int type = 6;
        int threshold = 130;
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        Bitmap result = Bitmap.createBitmap(width, height, bitmap.getConfig());
        int alpha, red, green, blue;
        int sumR, sumG, sumB;
        int[][] pixels = new int[3][3];
        for (int y = 0; y < height - 2; ++y) {
            for (int x = 0; x < width - 2; ++x) {
                for (int i = 0; i < 3; ++i) {
                    for (int j = 0; j < 3; ++j) {
                        pixels[i][j] = bitmap.getPixel(x + i, y + j);
                    }
                }
                alpha = Color.alpha(pixels[1][1]);
                sumR = sumG = sumB = 0;
                sumR = (type * Color.red(pixels[1][1])) - Color.red(pixels[0][0]) - Color.red(pixels[0][2]) - Color.red(pixels[2][0]) - Color.red(pixels[2][2]);
                sumG = (type * Color.green(pixels[1][1])) - Color.green(pixels[0][0]) - Color.green(pixels[0][2]) - Color.green(pixels[2][0]) - Color.green(pixels[2][2]);
                sumB = (type * Color.blue(pixels[1][1])) - Color.blue(pixels[0][0]) - Color.blue(pixels[0][2]) - Color.blue(pixels[2][0]) - Color.blue(pixels[2][2]);
                red = (int) (sumR + threshold);
                if (red < 0) {
                    red = 0;
                } else if (red > 255) {
                    red = 255;
                }
                green = (int) (sumG + threshold);
                if (green < 0) {
                    green = 0;
                } else if (green > 255) {
                    green = 255;
                }
                blue = (int) (sumB + threshold);
                if (blue < 0) {
                    blue = 0;
                } else if (blue > 255) {
                    blue = 255;
                }
                result.setPixel(x + 1, y + 1, Color.argb(alpha, red, green, blue));
            }
        }
        return result;
    }

    public static Bitmap contrast(Bitmap bitmap, int scale) {
        double deltaIndex[] = {
                0, 0.01, 0.02, 0.04, 0.05, 0.06, 0.07, 0.08, 0.1, 0.11,
                0.12, 0.14, 0.15, 0.16, 0.17, 0.18, 0.20, 0.21, 0.22, 0.24,
                0.25, 0.27, 0.28, 0.30, 0.32, 0.34, 0.36, 0.38, 0.40, 0.42,
                0.44, 0.46, 0.48, 0.5, 0.53, 0.56, 0.59, 0.62, 0.65, 0.68,
                0.71, 0.74, 0.77, 0.80, 0.83, 0.86, 0.89, 0.92, 0.95, 0.98,
                1.0, 1.06, 1.12, 1.18, 1.24, 1.30, 1.36, 1.42, 1.48, 1.54,
                1.60, 1.66, 1.72, 1.78, 1.84, 1.90, 1.96, 2.0, 2.12, 2.25,
                2.37, 2.50, 2.62, 2.75, 2.87, 3.0, 3.2, 3.4, 3.6, 3.8,
                4.0, 4.3, 4.7, 4.9, 5.0, 5.5, 6.0, 6.5, 6.8, 7.0,
                7.3, 7.5, 7.8, 8.0, 8.4, 8.7, 9.0, 9.4, 9.6, 9.8,
                10.0
        };
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        Bitmap bmpContrast = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bmpContrast);
        Paint paint = new Paint();
        ColorMatrix cm = new ColorMatrix();
        float x = 0.0f;
        if (scale < 0) {
            x = 127 + (float) scale / 100 * HUE_CONTRAST;
        } else {
            x = scale % 1;
            if (x == 0) {
                x = (float) deltaIndex[scale];
            } else {
                x = (float) deltaIndex[(scale << 0)] * (1 - x) + (float) deltaIndex[(scale << 0) + 1] * x; // use linear interpolation for more granularity.
            }
            x = x * 127 + 127;
        }
        float[] mat = new float[]
                {
                        x / HUE_CONTRAST, 0, 0, 0, 0.5f * (HUE_CONTRAST - x),
                        0, x / HUE_CONTRAST, 0, 0, 0.5f * (HUE_CONTRAST - x),
                        0, 0, x / HUE_CONTRAST, 0, 0.5f * (HUE_CONTRAST - x),
                        0, 0, 0, 1, 0,
                        0, 0, 0, 0, 1
                };
        cm.postConcat(new ColorMatrix(mat));
        ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
        paint.setColorFilter(f);
        c.drawBitmap(bitmap, 0, 0, paint);
        return bmpContrast;
    }

    public static Bitmap modifyOrientation(Bitmap bitmap, String image_url) throws IOException {
        ExifInterface ei = new ExifInterface(image_url);
        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                return rotate(bitmap, 90);
            case ExifInterface.ORIENTATION_ROTATE_180:
                return rotate(bitmap, 180);
            case ExifInterface.ORIENTATION_ROTATE_270:
                return rotate(bitmap, 270);
            case ExifInterface.ORIENTATION_FLIP_HORIZONTAL:
                return flip(bitmap, true, false);
            case ExifInterface.ORIENTATION_FLIP_VERTICAL:
                return flip(bitmap, false, true);
            default:
                return bitmap;
        }
    }

    public static Bitmap flip(Bitmap bitmap, boolean horizontal, boolean vertical) {
        Matrix matrix = new Matrix();
        matrix.preScale(horizontal ? -1 : 1, vertical ? -1 : 1);
        Bitmap bm = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        bitmap.recycle();
        return bm;
    }

    public static String saveBitmapToSdcard(Bitmap bitmap) throws IOException {
        String dirPath = Environment.getExternalStorageDirectory().getAbsolutePath() +
                File.separator + ConstActivity.ROOT_FOLDER +
                File.separator + FOLDER_NAME;
        File dir = new File(dirPath);
        if (!dir.exists())
            dir.mkdirs();

        String fileName = FILE_NAME + String.valueOf(System.currentTimeMillis()) + IMAGE_TYPE;
        File file = new File(dir, fileName);
        FileOutputStream fos = new FileOutputStream(file, true);
        BufferedOutputStream bos = new BufferedOutputStream(fos);
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
        bos.flush();
        bos.close();
        return file.getAbsolutePath();
    }
}
