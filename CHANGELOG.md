# Changelog
All notable changes in this project will be documented in this file.

## [UNRELEASED]
### Added
- add human date parser for future and past

## [1.1.0]
### Added
- add YamlFactory

## [1.0.0]
### Changed
- update to qsf-dto 1.0.0
- copy/ refactor classes from qsc-admin, qsf-integration

## [0.7] - 2021-05-20
### Added
- add SearchFilterMatcher
- use qsf-dto dependency

### Security
- fix CVE-2020-13956 in apache http client

## [0.6] - 2020-12-15
### Added
- implement a method to get the available text transformer filters
- make it possible to use lucene filters

### Fixed
- fix uppercase filter registration
- quote json as string and remove multiple whitespaces in filter

## [0.5] - 2020-09-17
### Added
- implement text util to strip all chars that are no characters or digits

## [0.4] - 2020-09-10
### Added
- add a text transformer implementation

### Deprecated
- TextNormalizerService - use the TextTransformer instead
- NormalizerConfig - use the TextTransformer instead

## [0.3]
### Added
- add embedDoc method
- implement a value 2 filename method for elastic

## [0.2] - 2020-07-13
### Added
- add normalizeToken function to get the token instead of the whole text
- add containsLetters helper function
- improve normalization service for numbers

## [0.1]
### Added
- initial commit
