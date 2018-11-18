#pragma version(1)
#pragma rs java_package_name(com.test.photoeditor)
#pragma rs_fp_relaxed

const static uchar gRed = 0;
const static uchar gYellow = 60;
const static uchar gGreen = 120;
const static uchar gAqua = 180;
const static uchar gBlue = 240;
const static uchar gPurple = 300;

const static uchar gBorderSize = 30;

const static uchar gRYBorder = gRed + gBorderSize;
const static uchar gYGBorder = gYellow + gBorderSize;
const static uchar gGABorder = gGreen + gBorderSize;
const static uchar gABBorder = gAqua + gBorderSize;
const static uchar gBPBorder = gBlue + gBorderSize;
const static uchar gPRBorder = gPurple + gBorderSize;


typedef struct ColorFilter {
    float hue;
    float saturation;
    float lightness;
} ColorFilter_t;

ColorFilter_t redFilter;
ColorFilter_t yellowFilter;
ColorFilter_t greenFilter;
ColorFilter_t aquaFilter;
ColorFilter_t blueFilter;
ColorFilter_t purpleFilter;

void setRedFilter(float hue, float saturation, float lightness) {
    redFilter.hue = hue;
    redFilter.saturation = saturation;
    redFilter.lightness = lightness;
}

void setYellowFilter(float hue, float saturation, float lightness) {
    yellowFilter.hue = hue;
    yellowFilter.saturation = saturation;
    yellowFilter.lightness = lightness;
}

void setGreenFilter(float hue, float saturation, float lightness) {
    greenFilter.hue = hue;
    greenFilter.saturation = saturation;
    greenFilter.lightness = lightness;
}

void setAquaFilter(float hue, float saturation, float lightness) {
    aquaFilter.hue = hue;
    aquaFilter.saturation = saturation;
    aquaFilter.lightness = lightness;
}

void setBlueFilter(float hue, float saturation, float lightness) {
    blueFilter.hue = hue;
    blueFilter.saturation = saturation;
    blueFilter.lightness = lightness;
}

void setPurpleFilter(float hue, float saturation, float lightness) {
    purpleFilter.hue = hue;
    purpleFilter.saturation = saturation;
    purpleFilter.lightness = lightness;
}

static float hueToColor(float p, float q, float t) {
    if (t < 0.0f) t += 1.0f;
    if (t > 1.0f) t -= 1.0f;
    if (t < 1.0f / 6.0f) return p + (q - p) * 6.0f * t;
    if (t < 1.0f / 2.0f) return q;
    if (t < 2.0f / 3.0f) return p + (q - p) * (2.0f / 3.0f - t) * 6.0f;
    return p;
}

uchar4 __attribute__((kernel)) filter(uchar4 in) {

    // convert color to float
    float red = in.r / 255.0f;
    float green = in.g / 255.0f;
    float blue = in.b / 255.0f;

    // convert rgb to hsl
    float minRGB = min( red, min( green, blue ) );
    float maxRGB = max( red, max( green, blue ) );
    float deltaRGB = maxRGB - minRGB;

    float hue = 0.0;
    float lightness = (maxRGB + minRGB) / 2.0f;
    float saturation = lightness > 0.5f ? deltaRGB / (2.0f - maxRGB - minRGB) : deltaRGB / (maxRGB + minRGB);

    if (deltaRGB != 0) {

        if (red == maxRGB) {
            hue = (green - blue) / deltaRGB;
        }
        else {
            if (green == maxRGB) {
                hue = 2 + (blue - red) / deltaRGB;
            }
            else {
                hue = 4 + (red - green) / deltaRGB;
            }
        }

        hue *= 60;
        if (hue < 0) { hue += 360; }
        if (hue == 360) { hue = 0; }
    }

    // filter colors
    if (hue >= 330 || hue < 30) {
        hue = hue - 30 + 60 * (redFilter.hue);
        saturation = saturation - 0.3 + 0.6 * (redFilter.saturation);
        lightness = lightness - 0.3 + 0.6 * (redFilter.lightness);
    } else if (hue >= 30 && hue < 90) {
        hue = hue - 30 + 60 * (yellowFilter.hue);
        saturation = saturation - 0.3 + 0.6 * (yellowFilter.saturation);
        lightness = lightness - 0.3 + 0.6 * (yellowFilter.lightness);
    } else if (hue >= 90 && hue < 150) {
        hue = hue - 30 + 60 * (greenFilter.hue);
        saturation = saturation - 0.3 + 0.6 * (greenFilter.saturation);
        lightness = lightness - 0.3 + 0.6 * (greenFilter.lightness);
    } else if (hue >= 150 && hue < 210) {
        hue = hue - 30 + 60 * (aquaFilter.hue);
        saturation = saturation - 0.3 + 0.6 * (aquaFilter.saturation);
        lightness = lightness - 0.3 + 0.6 * (aquaFilter.lightness);
    } else if (hue >= 210 && hue < 270) {
        hue = hue - 30 + 60 * (blueFilter.hue);
        saturation = saturation - 0.3 + 0.6 * (blueFilter.saturation);
        lightness = lightness - 0.3 + 0.6 * (blueFilter.lightness);
    } else if (hue >= 270 && hue < 330) {
        hue = hue - 30 + 60 * (purpleFilter.hue);
        saturation = saturation - 0.3 + 0.6 * (purpleFilter.saturation);
        lightness = lightness - 0.3 + 0.6 * (purpleFilter.lightness);
    }

    // range values
    if (hue < 0.0f) { hue += 360.0f; }
    if (hue >= 360.0f) { hue -= 360.0f; }
    if (saturation < 0.0f) { saturation = 0.0f; }
    if (saturation > 1.0f) { saturation = 1.0f; }
    if (lightness < 0.0f) { lightness = 0.0f; }
    if (lightness > 1.0f) { lightness = 1.0f; }

    // convert hsl to rgb
    float h = hue / 360.0;
    float r = 0.0;
    float g = 0.0;
    float b = 0.0;

    if (saturation == 0) {
        r = g = b = lightness;
    } else {
        float q = lightness < 0.5 ? lightness * (1 + saturation) : lightness + saturation - lightness * saturation;
        float p = 2 * lightness - q;

        r = hueToColor(p, q, h + 1.0f / 3.0f);
        g = hueToColor(p, q, h);
        b = hueToColor(p, q, h - 1.0f / 3.0f);
    }

    uchar4 out;
    out.r = r * 255;
    out.g = g * 255;
    out.b = b * 255;

    return out;
}