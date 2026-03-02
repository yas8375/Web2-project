# Page snapshot

```yaml
- generic [ref=e2]:
    - banner [ref=e3]:
        - link "Home" [ref=e4] [cursor=pointer]:
            - /url: /main
        - link "Movies" [ref=e5] [cursor=pointer]:
            - /url: /movies
        - link "Genres" [ref=e6] [cursor=pointer]:
            - /url: /browse/genres
        - link "Titles" [ref=e7] [cursor=pointer]:
            - /url: /browse/titles
        - link "Cart" [ref=e8] [cursor=pointer]:
            - /url: /cart
        - link "Login" [ref=e9] [cursor=pointer]:
            - /url: /login
    - main [ref=e10]:
        - generic [ref=e12]:
            - heading "Checkout" [level=1] [ref=e13]
            - generic [ref=e14]:
                - textbox "First name" [ref=e15]: Raghad
                - textbox "Last name" [ref=e16]: Alyousfy
                - textbox "Card number" [ref=e17]: '1234567890123456'
                - textbox "YYYY-MM-DD" [ref=e18]: 2030-12-31
                - button "Pay" [active] [ref=e19]
            - paragraph
```
