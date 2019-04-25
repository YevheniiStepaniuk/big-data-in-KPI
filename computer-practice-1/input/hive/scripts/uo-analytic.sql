use dm;
select name, edrpou, address,
row_number() over (partition by address order by edrpou) as rn_by_place
from UO_table
limit 20;