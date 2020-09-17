# Changelog
All notable changes in this project will be documented in this file.

## [UNRELEASED]
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
