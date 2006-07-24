-- Orphaned status records.
select p.name as orphaned_status, s.*
from status s
left join episode e
on (s.program_id = e.program_id
   and s.season = e.season
   and s.episode_number = e.number)
left join program p
on s.program_id = p.id
where e.program_id is null;

-- Programs no one has subscribed to.
select p.name as not_subscribed
from program p
left join subscribed s
on p.id = s.program_id
where s.program_id is null;

-- Programs without a source url.
select name as no_source_url
from program
where url is null
or url = "";

-- Programs that have not been updated.
select name as no_update
from program
where last_update is null;
