// =====================================================
// PayU Jenkins Bootstrap Seed Job
// Creates CI pipelines for all services
// =====================================================


// ---------- GLOBAL DEFAULT ENV ----------
def GLOBAL_ENV = [
  ORG_NAME: 'payu',
  DEFAULT_REGION: 'ap-south-1',
  LOG_LEVEL: 'INFO'
]

// ---------- LEAD-ASSIST EXTRA ENV ----------
def LEAD_ASSIST_ENV = [
  PRODUCT_TYPE: 'lead-management',
  LEAD_SOURCE: 'internal'
]

// ---------- APPLICATION CATALOG ----------
def APPS = [

  // Insurance → Health
  [name: 'policy-service', bu: 'insurance', domain: 'health'],
  [name: 'claim-service',  bu: 'insurance', domain: 'health'],

  // Insurance → Motor
  [name: 'motor-claim-service', bu: 'insurance', domain: 'motor'],

  // Lead-Assist
  [name: 'lead-api',       bu: 'lead-assist'],
  [name: 'lead-processor', bu: 'lead-assist'],
  [name: 'notification-service', bu: 'lead-assist']
]


// =====================================================
// CREATE CI JOBS
// =====================================================
APPS.each { app ->

  // ---- Merge environment variables ----
  def envVars = [:] + GLOBAL_ENV

  if (app.bu == 'lead-assist') {
    envVars += LEAD_ASSIST_ENV
  }

  // ---- Build hyphen-style job name ----
  def parts = [
    'payu',
    app.bu,
    app.domain,
    app.name,
    'ci'
  ].findAll { it }        // remove nulls

  def jobName = parts.join('-')


  // ---- Create Jenkins Pipeline Job ----
  pipelineJob(jobName) {

    description("Auto-generated CI pipeline for ${app.name}")

    definition {
      cpsScm {
        scm {
          git {
            remote {
              url("https://github.com/payu/${app.name}.git")
            }
            branch('main')
          }
        }
        scriptPath('Jenkinsfile')
      }
    }

    // ---- Inject environment variables ----
    environmentVariables {
      envVars.each { k, v ->
        env(k, v)
      }
    }

    // ---- Log rotation (enterprise hygiene) ----
    logRotator {
      daysToKeep(14)
      numToKeep(20)
    }
  }
}
