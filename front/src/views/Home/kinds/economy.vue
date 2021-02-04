<template>
  <recommend v-bind:kind="kind"></recommend>
</template>

<script>
import Recommend from "@/views/Home/recommend";
export default {
    name:'economy',
  components: {Recommend},
  data(){
      return {
        kind: '1'
    }
  }
}
</script>

<style scoped>

</style>

