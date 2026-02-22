<script setup>
import { default as axios } from 'axios'
import { Form, Field } from 'vee-validate'
import * as Yup from 'yup'

import { Alert } from '@/components'
import { useAlertStore } from '@/stores'

const schema = Yup.object().shape({
    oldPassword: Yup.string().required('Old password is required'),
    newPassword: Yup.string().required('New password is required'),
    newPasswordDup: Yup.string().required('Enter new password once again')
});

async function onSubmit(passwords, actions) {
  const alert = useAlertStore()
  if (passwords.newPassword !== passwords.newPasswordDup) {
    alert.error("Passwords do not match")
  } else {
    const resp = await axios.post("http://localhost:8080/change-password", 
      { oldPassword: passwords.oldPassword, newPassword: passwords.newPassword }, 
      { withCredentials: true })
                
    console.log(passwords.oldPassword, passwords.oldPasswordDup)
    console.log(resp)
    if (200 <= resp.status || resp.status < 300) {
      alert.success("Your password has been successfully updated")
    }
  }

  /*
  // docs: https://vee-validate.logaretm.com/v4/guide/components/handling-forms/
  actions.setValues({
    oldPassword: '',
    newPassword: '',
    newPasswordDup: '',
  });
  actions.setErrors({
    oldPassword: null,
    newPassword: null,
    newPasswordDup: null,
  });
  */

}

</script>

<template>
  <div id="page">
    <h1>Change password</h1>
    <Form @submit="onSubmit" :validation-schema="schema" v-slot="{ errors, isSubmitting }">

      <div class="form-group">
          <label>Old password:</label>
          <Field name="oldPassword" type="password" class="form-control" :class="{ 'is-invalid': errors.oldPassword }" />
          <div v-show="errors.oldPassword" class="invalid-feedback">
            <span>{{ errors.oldPassword }}</span>
          </div>
      </div>

      <div class="form-group">
          <label>New password:</label>
          <Field name="newPassword" type="password" class="form-control" :class="{ 'is-invalid': errors.newPassword }" />
          <div v-show="errors.newPassword" class="invalid-feedback">
            <span>{{ errors.newPassword }}</span>
          </div>
      </div>

      <div class="form-group">
          <label>Repeat new password:</label>
          <Field name="newPasswordDup" type="password" class="form-control" :class="{ 'is-invalid': errors.newPasswordDup }" />
          <div v-show="errors.newPasswordDup" class="invalid-feedback">
            <span>{{ errors.newPasswordDup }}</span> 
          </div>
      </div>

      <div class="form-group">
          <button :disabled="isSubmitting">
              <span v-if="isSubmitting">Submitting...</span>
              <span v-else>Submit changes</span>
          </button>
      </div>

    </Form>

    <Alert />

  </div>

</template>

<style scoped>
#page {
  padding-top: 2em;
  padding-left: 2em;

  display: flex;
  flex-direction: column;
  align-items: flex-start;
}


.form-group {
  display: flex;
  flex-direction: column;
  row-gap: .5em;
  margin-bottom: 0.5em;
}

.form-group > label {
  font-size: 0.8em;
}

.form-group > button {
  background: transparent;
  padding: 0.5em 1em;
  border: 3px solid black;
  border-radius: 5px;
  text-decoration: none;
  color: black;
}

.form-group > button:hover {
  background-color: #D8DDEF;
  cursor: pointer;
}

.form-group > button:active {
  background-color: #b1b5c4;
}

.invalid-feedback {
  font-size: 0.8em;

  background-color: #ffad99;
  color: #8d0801;
  border: 3px solid #8d0801;
  border-radius: 5px;
  padding: 0.3em 0.2em;

}

.is-invalid {
  border: 3px solid black;
  border-radius: 5px;
}


</style>
