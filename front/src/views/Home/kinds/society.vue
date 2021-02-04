<template>
  <div>
    <recommend v-bind:kind="kind"></recommend>
  </div>


</template>

<script>
import Recommend from "@/views/Home/recommend";
export default {
    name:'society',
  components: {Recommend},
  data(){
      return{
        kind: '3'
      }
  }
}
</script>

<style scoped>

</style>
