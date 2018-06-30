<#assign subject>You have been assigned to RFR workflow '${workflow.id}'</#assign>
<#assign text_body>

Dear ${recipient.firstName} ${recipient.lastName},

${sender} has assigned you to a workflow with ID ${workflow.id} in RFR.

You can access the project at the following location: ${appUrl}/rfrWorkflow/workflow/${workflow.id}

ESN Details: ${workflow.equipSerialNumber} & Outage Details: ${workflow.outageId?c}

</#assign>