<script setup>
import { Form, Field } from 'vee-validate';
import * as Yup from 'yup';

import { useAuthStore } from '@/stores';
import { Alert } from '@/components'

const schema = Yup.object().shape({
    username: Yup.string().required('Username is required'),
    password: Yup.string().required('Password is required')
});

async function onSubmit(values) {
    const authStore = useAuthStore();
    const { username, password } = values;
    console.log(`Calling onSubmit on form with email(${username}) and password(${password})`)
    await authStore.login(username, password);
}
</script>

<template>
  <div class="page-container">
    <div class="form-container">
      <div class="header">
        <h2>Login</h2>

        <p>
          Enter your credentials
        </p>
      </div>

      <Form @submit="onSubmit" :validation-schema="schema" v-slot="{ errors, isSubmitting }">
        <div class="form-group">
            <label>Email</label>
            <Field name="username" type="text" class="form-control" :class="{ 'is-invalid': errors.username }" />
            <div class="invalid-feedback">{{ errors.username }}</div>
        </div>
        <div class="form-group">
            <label>Password</label>
            <Field name="password" type="password" class="form-control" :class="{ 'is-invalid': errors.password }" />
            <div class="invalid-feedback">{{ errors.password }}</div>
        </div>
        <div class="form-group">
            <button class="btn btn-primary" :disabled="isSubmitting">
                <span v-show="isSubmitting" class="spinner-border spinner-border-sm mr-1"></span>
                Login
            </button>
        </div>
      </Form>

      <Alert />

    </div>
  </div>
</template>

<style scoped>
.page-container {
  display: flex;
  flex-direction: column;
  height: 100%;
  width: 100%;
  justify-content: center;
  align-items: center;
}

.form-container {
  display: flex;
  flex-direction: column;
  justify-content: space-evenly;

  min-height: 45%;
  width: 35%;

  border: 1px solid black;
  border-radius: 10px;
  padding: 1em;
  box-shadow: 5px 3px 3px gray;
}

.header {
  display: flex;
  flex-direction: column;
  align-items: start;
}

.form-group {
  display: flex;
  flex-direction: column;

  margin: 0.5em 0;
}



</style>
