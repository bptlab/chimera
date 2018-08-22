# Contributing to Chimera

Thanks for taking the time to contribute to the Chimera project :+1: 

This guide explains how you can contribute to the project.

## Vision

Chimera is an engine for executing case models. 
It implements the [fragment-based Case Management approach](https://bpt.hpi.uni-potsdam.de/Public) that was developed at the [*Business Process Technology* (BPT) group](https://bpt.hpi.uni-potsdam.de/Public).
Being developed at an university, Chimera is a research prototype, that aims to be useful for research & teaching.
We try to follow established open source principles to teach them to our students.

:notebook: You can read more about our vision [in the wiki](https://github.com/bptlab/chimera/wiki/ProductVision)

## Reporting Bugs
This section explains how you can submit a good bug report, which is *understandable* and *reproducible*.

#### Before submitting a bug report
- make sure that the bug occurs in the latest version of Chimera. Try whether it can be reproduced on https://bpt-lab.org/chimera-dev, which reflects the `HEAD` of the dev branch. Ideally, you also checkout the dev branch and build it locally (if you have not setup a development environment you can skip this step).
- if the bug only occurs in your local installation and not in the deployed version, it is most likely related to your configuration. Check the [troubleshooting section in the wiki]() for common sources of errors and how to solve them. If this does not help, go ahead and submit a bug report.
- please check whether the bug has already been reported. You can see all open bugs [here](https://github.com/bptlab/chimera/labels/bug).

#### How do I submit a good bug report?
Bugs are tracked a github issues with the label "bug". Create a new issue in the repository, add the label "bug", copy [the template](BUG_TEMPLATE.md) and fill out the information.

- **Provide a clear and descriptive title** for the issue to identify the problem.
- **Describe the problem**. What behavior did you observe? What behavior did you expect? 
- **Provide the exact steps which reproduce the problem** in as many details as possible. Can the problem be reliably be reproduced? How often does it occur, in which circumstances?
- **Include examples**, e.g. the case model which caused the problem.
- **Include screenshots** when possible.
- **Include details about your configuration and environment**, which OS are you on? Which version of Java and MySQL server do you use? Which version of Tomcat do you use, are you using a different application server? Did you change the `config.properties`, how?

## Suggesting Enhancements
This section describes how you can suggest enhancements, both completely new features and improvements to existing functionality.

### Before submitting a request for an enhancement
- make sure that the enhancement you want to suggest is not already implemented in the latest version of Chimera. Try whether it out on  https://bpt-lab.org/chimera-dev, which reflects the `HEAD` of the dev branch. Ideally, you also checkout the dev branch and build it locally (if you have not setup a development environment you can skip this step).
- please check whether the enhancement (or a similar one) has already been suggested. You can see all open features [here](https://github.com/bptlab/chimera/labels/feature). If this is the case, please add a comment supporting the suggested enhancement.

### How do I submit a request for an enhancement?
Enhancements are tracked as github issues with the label "feature". Create a new in the repository, add the label "feature", copy [the template](ENHANCEMENT_TEMPLATE.md) and fill out the information.

- **Provide a clear and descriptive title** for the issue to identify what the enhancement should achieve.
- **Describe the enhancement in a "user story" style**, e.g. "As a case worker I want to \[...\] in order to \[...\]" to motivate the need for the enhancement.
- **Explain in detail how the enhancement is expected to work**. This should be done from a user perspective.
- **Explain how the enhancement would benefit users** of Chimera. Mention the usecases in which it would be useful and to which user groups.
- **(Optionally) describe how the enhancement could be implemented**. Of course, you can skip this step, if you are unsure about how it could be implemented.

## Creating Pull Requests

## Finding Help

