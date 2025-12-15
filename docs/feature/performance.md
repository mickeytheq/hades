# Performance

The following is a performance comparison between AHLCG plugin and Hades.

These are intended as an illustration only as hardware varies.

## Card loading from disk

This test compares the time to load a component from disk

The following conditions were used

- A neutral Asset card
- Each test was run 5 times
- Each test ran **100** iterations (times below are for all iterations)
- Results are in seconds (lower is better)

| Plugin | Run 1 | Run 2 | Run 3 | Run 4 | Run 5 |
| --- | ----- | ----- | ---- | ----- | ----- | 
| AHLCG plugin | 11.09 | 10.84 | 10.78 | 10.70 | 10.71 |
|Hades | 2.46 | 2.27 | 2.24 | 2.24 | 2.23 |

**Summary - Hades is ~4-5 times faster**

## Editor/user interface creation

This test measures the time to create the editor interface (controls and preview panel)

The following conditions were used

- A neutral Asset card
- Each test was run 5 times
- Each test ran **100** iterations (times below are for all iterations)
- Results are in seconds (lower is better)

| Plugin | Run 1 | Run 2 | Run 3 | Run 4 | Run 5 |
| --- | ----- | ----- | ---- | ----- | ----- | 
| AHLCG plugin | 236.84 | Out of memory |  |  |  |
|Hades | 12.25 | 11.99 | 12.34 | 12.07 | 12.20 |

**Summary - Hades is ~19-20 times faster**

## Card rendering

This test measures how long to paint the card, both front and back face

The following conditions were used

- A neutral Asset card
- Each test was run 5 times
- Each test ran **100** iterations (times below are for all iterations)
- Results are in seconds (lower is better)

AHLCG plugin - 238.25, 237.90, 241.87, 240.59, 236.83

Hades - 25.20, 24.71, 24.93, 25.00, 24.78

**Summary - Hades is ~9-10 times faster**
