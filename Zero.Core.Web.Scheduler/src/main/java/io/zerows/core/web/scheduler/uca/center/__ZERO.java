package io.zerows.core.web.scheduler.uca.center;

import io.vertx.up.eon.em.EmJob;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

interface CACHE {
    ConcurrentMap<EmJob.JobType, Agha> AGHAS = new ConcurrentHashMap<>() {
        {
            this.put(EmJob.JobType.FIXED, new FixedAgha());
            this.put(EmJob.JobType.ONCE, new OnceAgha());
            this.put(EmJob.JobType.FORMULA, new FormulaAgha());
        }
    };
}


