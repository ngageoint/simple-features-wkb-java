# Change Log
All notable changes to this project will be documented in this file.
Adheres to [Semantic Versioning](http://semver.org/).

---

## 2.0.3 (TBD)

* TBD

## [2.0.2](https://github.com/ngageoint/simple-features-wkb-java/releases/tag/2.0.2) (07-08-2019)

* simple-features-java version 2.0.2

## [2.0.1](https://github.com/ngageoint/simple-features-wkb-java/releases/tag/2.0.1) (04-01-2019)

* simple-features-java version 2.0.1
* Eclipse project cleanup

## [2.0.0](https://github.com/ngageoint/simple-features-wkb-java/releases/tag/2.0.0) (05-17-2018)

* Simple Features refactor, geopackage-wkb-java refactored to be simple-features-wkb-java
* "Wkb" prefix dropped from the Geometry Reader and Writer classes
* Common simple features code moved to new dependency, [simple-features-java](https://github.com/ngageoint/simple-features-java). Requires package name changes.
* Geometry Codes WKB utility class

## [1.0.6](https://github.com/ngageoint/geopackage-wkb-java/releases/tag/1.0.6) (05-02-2018)

* MultiCurve and MultiSurface read support
* MultiCurve and MultiSurface write support as Extended Geometry Collections
* Geometry Collection utility methods for collection type checks and conversions
* Handle reading 2.5D geometry type codes

## [1.0.5](https://github.com/ngageoint/geopackage-wkb-java/releases/tag/1.0.5) (02-13-2018)

* Additional Curve Polygon support
* Geometry utilities: pointInPolygon, pointOnPolygonEdge, closedPolygon, pointOnLine, and pointOnPath
* Shamos-Hoey simple polygon detection

## [1.0.4](https://github.com/ngageoint/geopackage-wkb-java/releases/tag/1.0.4) (11-20-2017)

* Douglas Peucker algorithm for geometry simplification
* maven-gpg-plugin version 1.6

## [1.0.3](https://github.com/ngageoint/geopackage-wkb-java/releases/tag/1.0.3) (06-12-2017)

* Shortcut default constructors for Geometry objects without z or m values
* Geometry utilities including centroid, minimize for antimeridian support, and normalize
* Geometry copy methods and constructors

## [1.0.2](https://github.com/ngageoint/geopackage-wkb-java/releases/tag/1.0.2) (04-18-2016)

* Geometry to JSON Compatible object utility

## [1.0.1](https://github.com/ngageoint/geopackage-wkb-java/releases/tag/1.0.1) (11-20-2015)

* Added tests - [Issue #6](https://github.com/ngageoint/geopackage-wkb-java/issues/6)

## [1.0.0](https://github.com/ngageoint/geopackage-wkb-java/releases/tag/1.0.0) (09-15-2015)

* Initial Release
