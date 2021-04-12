# What we liked

* Lack of documentation and suggested adding a guide was mentioned

* API evaluation and its reponses to errors, noticed how some of the endpoints do not respond with appropriate error messages when presented with incorrect payloads.

* Outlined code organization and how it could be improved

* Noticed that the validator and boardLogic are unnecessarily injected twice

* Talked about the lack of proper encapsulation in a few of the classes

* Suggested generating different boards as an improvement


# What could be improved

* Review could have been more in depth overall

* Missed opportunity to comment on the lack of clarity of the sudoku validation algorithm, and how it could be improved. e.g.: splitting functions, renaming variables, removing unnecessary parameters etc...

* No comments on the design of the API (HTTP verbs, endpoint naming, etc...)

* No mention of concurrency issues arising from the use of a non-threadsafe Map implementation

* No notice of the memory leak - and potential DOS attack vector - due to never deleting any boards from the map

* Comment on the presence of unit tests, but it would have been better if Tiago had realised that they are quite shallow, and don't really test much.

* Missed chance to talk about the lack of application of SOLID principles throughout the code, and in particular of the breakage of the Single Responsibility Principle in BoardLogic, as it is in charge of both validation and creation of new boards.
