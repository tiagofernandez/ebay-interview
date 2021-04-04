# Things I like about the codebase

* There are unit tests, so it's easier to discover features and see how the app works.
It feels rather safe refactoring and experimenting with the code.

* Modules are loosely coupled due to dependency injection.

* `ExceptionMapper` is used for mapping exceptions to clean error messages.


# Problems/suggested improvements

## Codebase

* Needs a getting started guide, some general documentation with maybe [a diagram](https://yuml.me/edit/9d038d7e), and relevant Javadoc comments.
It would be great to have the main algorithm documented too.

* I had to add `javax.xml.bind:jaxb-api` as dependency in order to install and run tests., but maybe my Java EE environment wasn't properly set up in first place.

* The codebase lacks tooling for coding style & linting.
I'd suggest adding a code formatter like [Prettier](https://github.com/jhipster/prettier-java), and static code analyzers such as [PMD](https://pmd.github.io) or [SpotBugs](https://spotbugs.github.io).
Many trivial errors and warnings would be easily surfaced and fixed right away.

* Some test cases in `BoardTests` could be parameterized instead of duplicated.

## Structure

* `com.ebay.epd.sudoku`'s classes could be better organized in a way configuration and exception don't end up in the `resource` package.
Personally I'd favor a somewhat flat structure with dependencies always pointing inwards.

* We could have different strategies for generating boards, if we defined an interface for `BoardLogic`.

* `BoardLogic` would be better off split in two classes: one to generate boards, and another to perform validation.

* `SudokuService.boardLogic` should be injected, in order to decouple implementation from service.
Also there's no need to have both `boardLogic` and `validator` as separate instances, since `BoardLogic` has that functionality built-in.

## Best Practices

* `SudokuValidationException` should be serializable, since exceptions can be sent across the network without any special configuration.
That would lead to changing the `List` of errors to `LinkedList`, and `InvalidFieldError` will have to implement `Serializable` as well.

* The `/validate` endpoint should throw `SudokuValidationException` instead of a generic `Exception`.
Also, it shouldn't crash when an incomplete data structure was sent in the request body.
And when receiving invalid data (i.e. not JSON), it should reject the request with a client error.

* The `Board` class shouldn't have `dealsLink` declared as public attribute, for encapsulation's sake.
There are a few other places where class attributes should be private.

* `get/setFields()` shouldn't expose their multidimensional array directly.

## Functionality

* Created boards always return the same grid.
That's a problem because everyone gets to play exactly the same game.

* `SudokuService.boards` shouldn't be static.
Actually it should rely on a database, key-value store, or persistent cache.
Eventually we'll want to deploy that service to a cloud, so multiple web server instances will be delivering and validating boards.
If you fetch a board by ID and it's retrieved by another server, then you won't get the board you were just playing.

* When fetching a board with invalid ID, the endpoint should return bad request (400).


# Highest priority improvement

* Generate different boards upon request.
I think users would be more engaged if they could get more challenges, and maybe even choose a difficulty level.
We could also unlock better deals, depending on the level or how many games they played so far.
