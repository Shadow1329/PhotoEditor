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

static void setFilter(ColorFilter_t *filter, float h, float s, float l) {
    filter->hue = h;
    filter->saturation = s;
    filter->lightness = l;
}

void setRedFilter(float h, float s, float l) {
    setFilter(&redFilter, h, s, l);
}

void setYellowFilter(float h, float s, float l) {
    setFilter(&yellowFilter, h, s, l);
}

void setGreenFilter(float h, float s, float l) {
    setFilter(&greenFilter, h, s, l);
}

void setAquaFilter(float h, float s, float l) {
    setFilter(&aquaFilter, h, s, l);
}

void setBlueFilter(float h, float s, float l) {
    setFilter(&blueFilter, h, s, l);
}

void setPurpleFilter(float h, float s, float l) {
    setFilter(&purpleFilter, h, s, l);
}

static float3 rgbTohsl(uchar red1, uchar green1, uchar blue1) {
    // convert color to float
    float r = red1 / 255.0f;
    float g = green1 / 255.0f;
    float b = blue1 / 255.0f;

    float minRGB = min( r, min( g, b ) );
    float maxRGB = max( r, max( g, b ) );
    float deltaRGB = maxRGB - minRGB;

    float hue = 0.0;
    float lightness = (maxRGB + minRGB) / 2.0f;
    float saturation = lightness > 0.5f ? deltaRGB / (2.0f - maxRGB - minRGB) : deltaRGB / (maxRGB + minRGB);

    if (deltaRGB != 0) {

        if (r == maxRGB) {
            hue = (g - b) / deltaRGB;
        }
        else {
            if (g == maxRGB) {
                hue = 2 + (b - r) / deltaRGB;
            }
            else {
                hue = 4 + (r - g) / deltaRGB;
            }
        }

        hue *= 60;
        if (hue < 0) { hue += 360; }
        if (hue == 360) { hue = 0; }
    }

    float3 out = {hue, saturation, lightness};
    return out;
}

static float hueToColor(float p, float q, float t) {
    if (t < 0.0f) t += 1.0f;
    if (t > 1.0f) t -= 1.0f;
    if (t < 1.0f / 6.0f) return p + (q - p) * 6.0f * t;
    if (t < 1.0f / 2.0f) return q;
    if (t < 2.0f / 3.0f) return p + (q - p) * (2.0f / 3.0f - t) * 6.0f;
    return p;
}

static uchar4 hslTorgb(float hue, float saturation, float lightness) {
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

uchar4 __attribute__((kernel)) filter(uchar4 in) {

    // convert rgb to hsl
    float3 hsl = rgbTohsl(in.r, in.g, in.b);
    float hue = hsl.x;
    float saturation = hsl.y;
    float lightness = hsl.z;

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
    return hslTorgb(hue, saturation, lightness);
}