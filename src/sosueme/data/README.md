Some data:
- [boys-names](http://www.census.gov/genealogy/names/dist.male.first)
- [girls-names](http://www.census.gov/genealogy/names/dist.female.first)
- [hawaiian-places](http://en.wikipedia.org/wiki/List_of_places_in_Hawaii) -- cut and paste the table

    curl http://www.census.gov/genealogy/names/dist.male.first | head -n 1000 > boys-names.txt
    curl http://www.census.gov/genealogy/names/dist.female.first | head -n 1000 > girls-names.txt
