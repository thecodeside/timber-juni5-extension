# Timber Junit5 Extension
This library provide a Kotlin written JUnit5 Extension for tests that plants a temporary Timber tree. It pipes any logs sent via Timber to the standard System.out. Once a unit test has completed, the Timber tree is removed to avoid logging unintended test cases.

The rule is extremely useful for Android JUnit tests, as the Timber logs do not show without planting a tree.

## Usage
If you want to use library in default configuration it's extreamly easy:

```kotlin
@ExtendWith(TimberTestExtension::class)
class DefaultParametersTest {
```

You can also provide your own configutation

```kotlin
class CustomParametersTest {

    @JvmField
    @RegisterExtension
    val testExtension = TimberTestExtension(
        Rules(
            minPriority = Log.ERROR,
            showThread = true,
            showTimestamp = true,
            logOnlyWhenTestFails = true
        )
    )
}
```

### Configuration
As seen in the example above, there are many ways to modify the output using the following behaviours:
- The minimum log level to output.
- Whether thread ids are shown.
- Whether timestamps are shown.
- Whether to always log, or only log when a unit test fails.

## How to add to your project
##### Step 1. Add the JitPack repository to your build file

Add it in your root build.gradle at the end of repositories:

```groovy
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}
```

##### Step 2. Add the dependency

```groovy
dependencies {
    testImplementation 'com.github.thecodeside:timber-junit5-extension:1.0'
}
```

## Special thanks for the original author

Special thanks to **Lachlan McKee** who created the original version of this library for JUnit4. I only rewrote it in Kotlin for JUnit5.
You can find JUnit4 TestRule here version:
<https://github.com/LachlanMcKee/timber-junit-rule>
