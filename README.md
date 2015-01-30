# Automatische Tests für Android

Der Prozess der Qualitätssicherung von Software wird unter anderem durch automatische Testverfahren greifbar. Mit [Dagger](http://google.github.com/dagger), [Mockito](https://code.google.com/p/mockito/) und [Espresso](https://code.google.com/p/android-test-kit) lassen sich Tests für Android entwerfen, die die manuellen Aufwände sofort spürrbar reduzieren.

## Praktisches Beispiel

Eine überzeugende App muss auch mit Ausnahmefällen geschickt umgehen. Die Präsentationen von zielführenden Hinweisen zur Problemlösung ist ein konkretes Beispiel dafür. Kritische Zustände zu provozieren, kann in manuellen Tests mit unter schwierig werden. Mittels passend gewählter Software-Architektur und moderner Test-Strategien ist dies allerdings zuverlässig machbar. Das lässt eine überprüfbare Refaktorisierung zu und beschleunigt die Release-Zyklen.

Netzwerkverbindungsstatus prüfen
--------

In der Activity wird der ConnectivityManager mittels Dagger Dependency Injection bereitgestellt.

```java
private boolean isOffline() {

  final NetworkInfo activeNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
  return activeNetworkInfo == null || !activeNetworkInfo.isConnected();
}
```
  
Der UI Test mit Espresso ermöglicht das gewünschte Verhalten der App während der unterschiedlichen Verbindungszustände nachzuweisen.

```java
public void testShouldDisplayErrorDialog() {

  when(mConnectivityManager.getActiveNetworkInfo().isConnected()).thenReturn(false);

  onView(withId(R.id.action_send))
        .check(matches(isDisplayed()))
        .perform(click());

  onView(withText(mActivity.getString(R.string.error_title)))
        .check(matches(isDisplayed()));
}
```
![Screenshot](https://github.com/andnexus/android-testing/raw/master/screenshot.png "Screenshot")

## Continuous Integration

Nach Änderungen am Quellcode müssen die Variationen der App [gebaut und getestet](https://travis-ci.org/andnexus/android-testing) werden. Mit einem Pull-Request auf einen dedizierten Branch kann hiermit auch die Verteilung des Build-Artefakts im [Google Play Store](https://play.google.com) samt Listings wie Screenshots und Neuerungen angestoßen werden. [Mehr Erfahren...](http://www.andnexus.com)

[![Build Status](https://travis-ci.org/andnexus/android-testing.svg?branch=master)](https://travis-ci.org/andnexus/android-testing)

License
-------

Copyright 2014 The Android Open Source Project, Inc.

Licensed to the Apache Software Foundation (ASF) under one or more contributor
license agreements.  See the NOTICE file distributed with this work for
additional information regarding copyright ownership.  The ASF licenses this
file to you under the Apache License, Version 2.0 (the "License"); you may not
use this file except in compliance with the License.  You may obtain a copy of
the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
License for the specific language governing permissions and limitations under
the License.

