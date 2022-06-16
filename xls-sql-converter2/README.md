Как заполнять xls:

Партию можно записать только в LOTATTRIBUTE, таблица LOT заполнится автоматически
для отсутствующих партий

для балансов нужна только таблица SERIALINVENTORY,
остальные - LOTxLOCxID, SKUxLOC, LOT - заполняются автоматически

Если в заказе SCHEDULEDSHIPDATE is null, это поле рассчитается и проставится из SHIPMENTDATETIME

В base.xml есть таблица TRUNCATE, там список таблиц, подлежащих полному очищению

Записать в поле sql выражение: `sql:dateadd(hh, 10, getutcdate())`

Какие есть участки упаковки: 
```
select distinct ad2.AREAKEY, l2.PUTAWAYZONE, l2.LOCATIONTYPE, l2.LOC, cl.TYPE
from wmwhse1.LOC l1
    join wmwhse1.AREADETAIL ad1 on ad1.PUTAWAYZONE = l1.PUTAWAYZONE
    join wmwhse1.AREADETAIL ad2 on ad2.AREAKEY = ad1.AREAKEY
    join wmwhse1.loc l2 on l2.PUTAWAYZONE = ad2.PUTAWAYZONE
    left join wmwhse1.ConsolidationLocation cl on cl.LOC = l2.loc
where l1.LOCATIONTYPE = 'PACK'
order by 1, 2, 3, 4, 5
```