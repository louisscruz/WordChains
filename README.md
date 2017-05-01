# Word Chains

This project finds a chain of words that exists between a given source and target.

For example, given the input of `ding` and `camp`, it will return an `ArrayList` of `[ding, dine, dime, dame, came, camp]`.

## Levenshtein Chains

It's also possible to create chains that use Levenshtein distance. For instace:

```java
WordChains levenshteinWordChains = new WordChains(dictionary, true);
ArrayList<String> path = levenshteinWordChains.run("ding", "dinner");
```

In this case, `path` will be `[ding, dig, did, lid, lied, lined, liner, diner, dinner]`. It currently does not find the shortest path.