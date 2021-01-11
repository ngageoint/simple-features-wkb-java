# Simple Features WKB Java

#### Simple Features Well Known Binary Lib ####

The Simple Features Libraries were developed at the [National Geospatial-Intelligence Agency (NGA)](http://www.nga.mil/) in collaboration with [BIT Systems](https://www.caci.com/bit-systems/). The government has "unlimited rights" and is releasing this software to increase the impact of government investments by providing developers with the opportunity to take things in new directions. The software use, modification, and distribution rights are stipulated within the [MIT license](http://choosealicense.com/licenses/mit/).

### Pull Requests ###
If you'd like to contribute to this project, please make a pull request. We'll review the pull request and discuss the changes. All pull request contributions to this project will be released under the MIT license.

Software source code previously released under an open source license and then modified by NGA staff is considered a "joint work" (see 17 USC § 101); it is partially copyrighted, partially public domain, and as a whole is protected by the copyrights of the non-government authors and must be released according to the terms of the original open source license.

### About ###

[Simple Features WKB](http://ngageoint.github.io/simple-features-wkb-java/) is a Java library for writing and reading [Simple Feature](https://github.com/ngageoint/simple-features-java) Geometries to and from Well-Known Binary.

### Usage ###

View the latest [Javadoc](http://ngageoint.github.io/simple-features-wkb-java/docs/api/)

#### Read ####

```java

// byte[] bytes = ...

Geometry geometry = GeometryReader.readGeometry(bytes);
GeometryType geometryType = geometry.getGeometryType();

```

#### Write ####

```java

// Geometry geometry = ...

byte[] bytes = GeometryWriter.writeGeometry(geometry);

```

### Installation ###

Pull from the [Maven Central Repository](http://search.maven.org/#artifactdetails|mil.nga.sf|sf-wkb|2.0.3|jar) (JAR, POM, Source, Javadoc)

```xml

<dependency>
    <groupId>mil.nga.sf</groupId>
    <artifactId>sf-wkb</artifactId>
    <version>2.0.3</version>
</dependency>

```

### Build ###

[![Build & Test](https://github.com/ngageoint/simple-features-wkb-java/workflows/Build%20&%20Test/badge.svg)](https://github.com/ngageoint/simple-features-wkb-java/actions?query=workflow%3A%22Build+%26+Test%22)

Build this repository using Eclipse and/or Maven:

    mvn clean install

### Remote Dependencies ###

* [Simple Features](https://github.com/ngageoint/simple-features-java) (The MIT License (MIT)) - Simple Features Lib
