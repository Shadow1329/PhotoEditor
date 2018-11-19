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

    float hue = 0.0f;
    float lightness = (maxRGB + minRGB) / 2.0f;
    float saturation = lightness > 0.5f ? deltaRGB / (2.0f - maxRGB - minRGB) : deltaRGB / (maxRGB + minRGB);

    if (deltaRGB != 0.0f) {

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

static void applyFilter(ColorFilter_t *filter, float *h, float *s, float *l) {
    *h = *h - 30 + 60 * (filter->hue);
    *s = *s - 0.3 + 0.6 * (filter->saturation);
    *l = *l - 0.3 + 0.6 * (filter->lightness);
}

static void rangeValues(float *h, float *s, float *l) {
    if (*h < 0.0f) { *h += 360.0f; }
    if (*h >= 360.0f) { *h -= 360.0f; }
    if (*s < 0.0f) { *s = 0.0f; }
    if (*s > 1.0f) { *s = 1.0f; }
    if (*l < 0.0f) { *l = 0.0f; }
    if (*l > 1.0f) { *l = 1.0f; }
}

uchar4 __attribute__((kernel)) filter(uchar4 in) {

    // convert rgb to hsl
    float3 hsl = rgbTohsl(in.r, in.g, in.b);
    float hue = hsl.x;
    float saturation = hsl.y;
    float lightness = hsl.z;

    // filter colors
    if (hue >= 330 || hue < 30) {
        applyFilter(&redFilter, &hue, &saturation, &lightness);
    } else if (hue >= 30 && hue < 90) {
        applyFilter(&yellowFilter, &hue, &saturation, &lightness);
    } else if (hue >= 90 && hue < 150) {
        applyFilter(&greenFilter, &hue, &saturation, &lightness);
    } else if (hue >= 150 && hue < 210) {
        applyFilter(&aquaFilter, &hue, &saturation, &lightness);
    } else if (hue >= 210 && hue < 270) {
        applyFilter(&blueFilter, &hue, &saturation, &lightness);
    } else if (hue >= 270 && hue < 330) {
        applyFilter(&purpleFilter, &hue, &saturation, &lightness);
    }

    // range values
    rangeValues(&hue, &saturation, &lightness);

    // convert hsl to rgb
    return hslTorgb(hue, saturation, lightness);
}