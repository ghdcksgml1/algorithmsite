# algorithmsite

## ๐ ๋ฐ์ดํฐ๋ฒ ์ด์ค ์ค๊ณ

<img width="636" alt="แแณแแณแแตแซแแฃแบ 2022-04-29 แแฉแแฎ 8 19 16" src="https://user-images.githubusercontent.com/79779676/165934708-77984fa3-703e-4919-9585-56bdec9cb90b.png">

<br/><br/>

## ๐ ๋ฐฑ์ค ์นํฌ๋กค๋ง

Jsoup ๋ผ์ด๋ธ๋ฌ๋ฆฌ๋ฅผ ์ฌ์ฉํ์ฌ ์นํฌ๋กค๋ง์ ์งํํ๋ค. (๊ณตํต ํด๋์ค ์ฐพ๊ธฐ์ imgํ๊ทธ์์ src ๊ฐ์ ๋นผ์ค๋ ์ฝ๋๋ฅผ ์๊ฐํ๋๊ฒ ์ค๋๊ฑธ๋ ธ๋ค.)

๋ฐ์ดํฐ๊ฐ ์ด 20000๊ฐ ์ด์์ธ๋ฐ, ์ด๊ฑธ ํ๋ฒ์ ์ฒ๋ฆฌํ๋ ค๋๊น ๊ณ์ Exception์ด ํฐ์ ธ์ 100๊ฐ์ฉ ๋์ด์ ๋ฐ์ดํฐ๋ฒ ์ด์ค์ ์ถ๊ฐํ๋ค.

๊ทธ๋ฆผ๊ณผ๊ฐ์ด ๋ฐฑ์ค์ ๋ชจ๋  ๋ฌธ์ ๋ฅผ ๋ฐ์ดํฐ๋ฒ ์ด์ค์ ์ถ๊ฐํ๋ค.

<img width="700" alt="แแณแแณแแตแซแแฃแบ 2022-04-29 แแฉแแฎ 8 10 00" src="https://user-images.githubusercontent.com/79779676/165934037-5943a921-eb86-4f15-9be8-f45aea51ed21.png">

```java
    @Transactional
    public int refreshAllProblem(){
        try {
            int page_num = 1;
            while (true) {
                List<Problem> problems = new ArrayList<>();

                String CUR_URL = BOJ_PROBLEM_URL + page_num;
                Document document = Jsoup.connect(CUR_URL).userAgent("Mozilla").get();
                Elements problem_number = document.select(".hysUdN"); // ํด๋์ค๋ฅผ ์ด์ฉํด์ ๋ฌธ์  ๋ฒํธ ๊ฒ์
                Elements problem_name = document.select(".__Latex__"); // ํด๋์ค๋ฅผ ์ด์ฉํด์ ๋ฌธ์  ์ด๋ฆ ๊ฒ์
                Elements tier_url = document.select("img[src$=.svg]"); // imgํ๊ทธ์ src๊ฐ ๋์ด .svg๋๋๋ ํ๊ทธ ๊ฒ์

                if (tier_url.size() == 0) break; // ๊ฒ์๋๋๊ฒ ์์ผ๋ฉด ๋ฉ์ถ๊ธฐ
                
                for (int i = 0; i < tier_url.size(); i++) {
                    Problem problem = Problem.builder()
                            .problem_number(Integer.parseInt(problem_number.get(i).text())) // ๋ถํ์ํ ํ๊ทธ๋ฅผ ์ ๊ฑฐํ๊ณ  ํ์คํธ๋ถ๋ถ๋ง ์ถ์ถ
                            .problem_name(problem_name.get(i).text()) // ๋ถํ์ํ ํ๊ทธ๋ฅผ ์ ๊ฑฐํ๊ณ  ํ์คํธ๋ถ๋ถ๋ง ์ถ์ถ
                            .tier_url(tier_url.get(i).attr("src")) // tier_url ๊ฐ์ฒด์ src ์ต์ ์ ๋ณด๋ฅผ ๊ฐ์ ธ์ด.
                            .build();
                    problems.add(problem);
                }
                problemRepository.saveAll(problems); // 100๊ฐ์ฉ ์ ์ฅ
                page_num++;
            }
            return HttpStatus.OK.value();
        }catch (IOException e){
            log.warn("ProblemService refreshAllProblem IOException");
            e.printStackTrace();
            return HttpStatus.INTERNAL_SERVER_ERROR.value();
        }
    }
```
