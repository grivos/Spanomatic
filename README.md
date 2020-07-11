# Spanomatic
[![Download](https://api.bintray.com/packages/grivos/Spanomatic/Spanomatic/images/download.svg)](https://bintray.com/grivos/Spanomatic/Spanomatic/_latestVersion)  
Spanomatic is an Android library that allows you to automatically add spans to text from resources strings.

* [Getting Started](#getting-started)
  * [Dependencies](#dependencies)
  * [Initialization](#initialization)
* [String Resources Annotations](#string-resources-annotations)
  * [Supported Annotation Keys](#supported-annotation-keys)
    * [fgColor](#fgcolor)
    * [bgColor](#bgcolor)
    * [relativeTextSize](#relativetextsize)
    * [absoluteTextSize](#absolutetextsize)
    * [drawable](#drawable)
    * [format](#format)
    * [quote](#quote)
    * [bullet](#bullet)
    * [url](#url)
    * [leadingMargin](#leadingmargin)
    * [typeface](#typeface)
    * [click](#click)
  * [Adding Support For Custom User Annotation Keys](#adding-support-for-custom-user-annotation-keys)
* [Using Spanomatic Manually](#using-spanomatic-manually)
  * [Format Arguments](#format-arguments)
* [Acknowledgements](#acknowledgements)

## Getting Started

### Dependencies
Spanomatic uses the [ViewPump](https://github.com/InflationX/ViewPump) library, that provides an API for pre/post-inflation interceptors.
Add these dependencies to your app module's build.gradle file:
```groovy
dependencies {
    implementation 'com.grivos.spanomatic:spanomatic:1.0.2'
    implementation 'io.github.inflationx:viewpump:2.0.3'
}
```

### Initialization
Init ViewPump and add the SpanomaticInterceptor in your app class:
```kotlin
class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        ViewPump.init(
            ViewPump.builder()
                .addInterceptor(
                    SpanomaticInterceptor()
                )
                .build()
        )
    }
    
}
```

Wrap your activities context:
```kotlin
class BaseActivity : AppCompatActivity() {

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase))
    }

}
```

## String Resources Annotations

In order for Spanomatic to translate string resources annotations into spans, you need to wrap your strings in special annotations.
The mechanic is like so:
```xml
<string name="annotated_string">This part isn\'t annotated,  <annotation key="value">but this part is</annotation>.</string>
```
Spanomatic parses the string resource, looks for these annotations, and depending on the key and value, it adds the appropriate spans.
You can also nest annotations like so:
```xml
<string name="nested_annotations">This part isn\'t annotated,  <annotation key1="value1"><annotation key2="value2">but this part has two annotations</annotation></annotation>.</string>
```

Spanomatic supports several annotation keys out of the box, but you can also register handlers for custom annotation keys.

### Supported Annotation Keys

#### fgColor
**Description:** Adds a `ForegroundSpan`.  
**Possible Values:** Either a literal color hex (`"#9C27B0"`) or a color reference (`"@color/material_purple"`).  
**Example:**
```xml
<string name="fg_color_hex">This text has <annotation fgColor="#9C27B0">purple foreground</annotation></string>
```
![example_fg](https://github.com/grivos/Spanomatic/blob/master/media/example_fg.png)

#### bgColor
**Description:** Adds a `BackgroundSpan`.  
**Possible Values:** Either a literal color hex or a color reference.  
**Example:**
```xml
<string name="bg_color_hex">This text has <annotation bgColor="@color/material_green">green background</annotation></string>
```
![example_bg](https://github.com/grivos/Spanomatic/blob/master/media/example_bg.png)

#### relativeTextSize
**Description:** Adds a `RelativeSizeSpan`.  
**Possible Values:** The size multiplier.  
**Example:**
```xml
<string name="relative_size">This text is two times <annotation relativeTextSize="2">bigger</annotation></string>
```
![example_relative_size](https://github.com/grivos/Spanomatic/blob/master/media/example_relative_size.png)

#### absoluteTextSize
**Description:** Adds an `AbsoluteSizeSpan`.  
**Possible Values:** A size identifier. Either a literal (`18dp`, `16sp`, etc.) or a reference (`"@dimen/big_text_size"`).  
**Example:**
```xml
<string name="absolute_size">This text size is <annotation absoluteTextSize="20dp">20dp</annotation></string>
```
![example_absolute_size](https://github.com/grivos/Spanomatic/blob/master/media/example_absolute_size.png)

#### drawable
**Description:** Adds an `ImageSpan`.  
**Possible Values:** A drawable reference and an optional alignment (either `baseline` or `bottom`). If the alignment is missing, the default is `baseline`.<br/>The optional alignment is separated from the drawable reference using the pipe character (`|`).  
**Example:**
```xml
<string name="drawable_with_alignment">This is a <annotation drawable="@drawable/ic_cake_16dp|bottom">cake</annotation> drawable span.</string>
```
![example_drawable](https://github.com/grivos/Spanomatic/blob/master/media/example_drawable.png)

#### format
**Description:** Adds one of `StrikethroughSpan`, `StyleSpan`, `SuperscriptSpan`, `SubscriptSpan`, `UnderlineSpan`.  
**Possible Values:**  

| Value | Span | 
|---|---|
| strikethrough | `StrikethroughSpan()` |
| bold | `StyleSpan(Typeface.BOLD)` |
| italic | `StyleSpan(Typeface.ITALIC)` |
| boldItalic | `StyleSpan(Typeface.BOLD_ITALIC)` |
| superscript | `SuperscriptSpa()` |
| subscript | `SubscriptSpan()` |
| underline | `UnderlineSpan()` |

**Example:**
```xml
<string name="format_string">Spanomatic supports <annotation format="strikethrough">strikethrough</annotation>, <annotation format="bold">bold</annotation>, <annotation format="italic">italic</annotation>, <annotation format="boldItalic">bold italic</annotation>, <annotation format="superscript">superscript</annotation>, <annotation format="subscript">subscript</annotation> and <annotation format="underline">underline</annotation> spans.</string>
```
![example_format](https://github.com/grivos/Spanomatic/blob/master/media/example_format.png)

#### quote
**Description:** Adds a `QuoteSpan`.  
**Possible Values:** Either empty or an optional literal color hex or a color reference.  
**Example:**
```xml
<string name="quote"><annotation quote="">This is a quote</annotation></string>
```
```xml
<string name="quote_with_color"><annotation quote="#D81B60">This is a quote</annotation></string>
```
![example_quote](https://github.com/grivos/Spanomatic/blob/master/media/example_quote.png)

#### bullet
**Description:** Adds a `BulletSpan`.  
**Possible Values:** Either empty or an optional gap width (literal dimension or a dimension reference), and an optional literal color hex or a color reference.  
**Example:**
```xml
<string name="bullet"><annotation bullet="">This is a bullet</annotation></string>
```
```xml
<string name="bullet_with_gap"><annotation bullet="16dp">This is a bullet</annotation></string>
```
```xml
<string name="bullet_with_gap_and_color"><annotation bullet="16dp|#00574B">This is a bullet</annotation></string>
```
![example_bullet](https://github.com/grivos/Spanomatic/blob/master/media/example_bullet.png)

#### url
**Description:** Adds a `URLSpan`.  
**Possible Values:** A url.  
**Example:**
```xml
<string name="url">This text has a <annotation url="https://www.google.com/">UrlSpan</annotation></string>
```
![example_url](https://github.com/grivos/Spanomatic/blob/master/media/example_url.png)

#### leadingMargin
**Description:** Adds a `LeadingMarginSpan`.  
**Possible Values:** The size of the leading margin (either a literal dimension or a dimension reference).  
**Example:**
```xml
<string name="leading_margin"><annotation leadingMargin="@dimen/leading_margin">This text has a leading margin</annotation></string>
```
![example_leading_margin](https://github.com/grivos/Spanomatic/blob/master/media/example_leading_margin.png)

#### typeface
**Description:** Adds a `CustomTypefaceSpan`.  
**Possible Values:** The name of this typeface (either a literal string or a string reference).  
**Example:**  
First, you need to register a `TypefaceProvider` (the best place to do so is in your Application `onCreate()` method):
```kotlin
val latoRegular = ResourcesCompat.getFont(this, R.font.lato_regular)
val latoLight = ResourcesCompat.getFont(this, R.font.lato_light)
val latoBold = ResourcesCompat.getFont(this, R.font.lato_bold)
Spanomatic.typefaceProvider = object : TypefaceProvider {

    override fun getTypeface(name: String): Typeface? {
        return when (name) {
            "latoRegular" -> latoRegular
            "latoLight" -> latoLight
            "latoBold" -> latoBold
            else -> null
        }
    }

}
```
Then we can use these typefaces in a string resource:

```xml
<string name="typefaces">Here we have three different typefaces: <annotation typeface="latoRegular">Lato Regular</annotation>, <annotation typeface="latoLight">Lato Light</annotation> and <annotation typeface="latoBold">Lato Bold</annotation></string>
```
![example_typeface](https://github.com/grivos/Spanomatic/blob/master/media/example_typeface.png)

#### click
**Description:** Adds a `ListenableClickableSpan` (a custom `ClickableSpan` that can be registered with a click callback).<br/>Note that this span doesn't add an `UnderlineSpan`, so if that's what you want, you should wrap it in another `format="underline"` annotation.  
**Possible Values:** The name of this click (so we can register a callback to the span from the code).  
**Example:**  

```xml
<string name="click">This span is clickable: <annotation format="underline"><annotation click="click1">click me</annotation></annotation></string>
```

To catch the onClick event, you need to register a callback like so:
```kotlin
clickableTextView.addSpanClickListener("click1") {
    Toast.makeText(this, "Span was clicked", Toast.LENGTH_SHORT).show()
}
```
![example_click_toast](https://github.com/grivos/Spanomatic/blob/master/media/example_click_toast.gif)

###  Adding Support For Custom User Annotation Keys

You can register your own annotation span handlers like so:
```kotlin
Spanomatic.setAnnotationSpanHandler("myCustomKey") { annotationValue, context ->
    val myParsedValue = parseValue(annotationValue, context)
    MyCustomSpan(myParsedValue)
}
```

Then you can use it in your string resources:
```xml
<string name="custom">This span is <annotation myCustomKey="someValue">custom</annotation></string>
```

## Using Spanomatic Manually

Spanomatic automatically adds spans in the layout inflation process, but you can also use it manually like so:
```kotlin
val spanned = addSpansFromAnnotations(R.string.annotated_string)
myTextView.text = spanned
```

### Format Arguments

Spanomatic also supports format arguments:
```xml
<string name="annotated_string_with_parameters">This text has annotations with parameters: <annotation format="bold">%1$s</annotation>, <annotation format="italic">%2$b</annotation>, <annotation format="underline">%3$d</annotation></string>
```

```kotlin
val param1 = "text"
val param2 = false
val param3 = 5
val spanned = addSpansFromAnnotations(R.string.annotated_string_with_parameters, param1, param2, param3)
myTextView.text = spanned
```
## Acknowledgements

Spanomatic was inspired by [this blog post](https://medium.com/androiddevelopers/styling-internationalized-text-in-android-f99759fb7b8f) by Florina Muntenescu and the [Rialto](https://github.com/StylingAndroid/Rialto) library by Mark Allison.
