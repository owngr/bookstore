module.exports = {
  env: {
    node: true,
  },
  extends: [
    'eslint:recommended',
    'plugin:vue/vue3-recommended',
    'prettier',
  ],
  rules: {
    // 'semi': ['error','never'],
    'vue/require-default-prop': 'off',
    'vue/require-prop-types': 'off',
    // override/add rules settings here, such as:
    // 'vue/no-unused-vars': 'error'
  }
}
