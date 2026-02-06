# Exceptions

## Summary
Exceptions is a folder containing all exception classes that we have captured in our project.

## Classes

- **AnswerNotFoundException.java** - Exception thrown when an answer is not found (of a form by a user).
- **FormAlreadyAnsweredException.java** - Exception thrown when a form has already been answered by a user.
- **FormException.java** - General exception for form-related errors.
- **FormNotExecutedClustering.java** - Exception thrown when clustering has not been executed on a form.
- **FormTitleInvalid.java** - Exception thrown when a form title is invalid (e.g., empty, null or already used).
- **IdNotFoundException.java** - Exception thrown when an ID is not found.
- **InvalidAnswerChoiceException.java** - Exception thrown when an invalid answer choice is provided.
- **InvalidAnswerCountException.java** - Exception thrown when the number of answers provided is invalid.
- **InvalidAnswerFile.java** - Exception thrown when an answer file is invalid or cannot be processed.
- **InvalidAnswerFormatException.java** - Exception thrown when an answer format is invalid.
- **InvalidAnswerRangeException.java** - Exception thrown when an answer is out of the valid range.
- **InvalidChoicesException.java** - Exception thrown when the choices provided for a question are invalid (e.g., less than 2).
- **InvalidFormFile.java** - Exception thrown when a form file is invalid or cannot be processed.
- **InvalidFormStructureException.java** - Exception thrown when a form structure is invalid.
- **InvalidMaxChoicesException.java** - Exception thrown when the maximum number of choices for a question is invalid (e.g., less than 1 or greater than number of choices).
- **InvalidMultipleChoiceAnswerException.java** - Exception thrown when a multiple-choice answer is invalid (e.g., more options than maximum).
- **InvalidQuestionBoundsException.java** - Exception thrown when question bounds are invalid (e.g., min greater than max).
- **InvalidQuestionTextException.java** - Exception thrown when question text is invalid (e.g., empty or null).
- **InvalidQuestionTypeException.java** - Exception thrown when a question type is invalid or unsupported.
- **ProfileNotFoundException.java** - Exception thrown when a user profile is not found.
- **UsernameAlreadyExistsException.java** - Exception thrown when trying to create a user that already exists.