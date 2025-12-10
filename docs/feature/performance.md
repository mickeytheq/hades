# Performance

The following is a performance comparison between AHLCG plugin and Hades.

These are intended as an illustration only as hardware varies.

## Card loading from disk

This test compares the time to load a component from disk

The following conditions were used

- A neutral Asset card
- Each test was run 5 times
- Results are in seconds (lower is better)
- 100 iterations (times below are for all iterations)

AHLCG plugin - 11.09, 10.84, 10.78, 10.70, 10.71 seconds
Hades - 2.46, 2.27, 2.24, 2.24, 2.23 seconds

**Summary - Hades is ~4-5 times faster**

## Editor/user interface creation

This test measures the time to create the editor interface (controls and preview panel)

**TBD** - earlier tests showed ~10x performance improvement

## Card rendering

This test measures how long to paint the card, both front and back face

The following conditions were used

- A neutral Asset card
- Each test was run 5 times
- Results are in seconds (lower is better)
- 100 iterations (times below are for all iterations)

AHLCG plugin - 238.25, 237.90, 241.87, 240.59, 236.83
Hades - 25.20, 24.71, 24.93, 25.00, 24.78

**Summary - Hades is ~9-10 times faster**
