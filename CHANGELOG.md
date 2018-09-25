# Changelog
All notable changes to the Chimera case engine project will be documented in this file.

The format is based on [Keep a Changelog](http://keepachangelog.com/en/1.0.0/)
and this project adheres to [Semantic Versioning](http://semver.org/spec/v2.0.0.html).

## [1.5.0] - 2018-09-20

### added
- new [API version 3](https://github.com/bptlab/chimera/wiki/Chimera-API-v3) that includes management of organizations and users
- automatic generation of API specification using swagger
- fragment instantiation policy that includes the behaviors sequential and concurrent and an optional limit of instantiations
- roles for activities are now parsed and stored
- the role a user has is now checked in the backend before executing activities
- added files on how to contribute and templates for bug reports, feature requests, and pull requests

### changed
- annotation of API methods in source code changed to [OpenAPI 3 format](https://github.com/OAI/OpenAPI-Specification/tree/3.0.0) 
- extensive re-structuring of the wiki

### fixed
- fix the SSE bug
- fix locking of data objects with respect to boundary events

### deprecated
- API version 2 should no longer be used

## [1.4.0] - 2018-08-05

This is the baseline of the changelog. All changes in the future version will be documented in this file.
