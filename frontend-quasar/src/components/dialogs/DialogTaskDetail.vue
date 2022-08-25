<template>
  <q-dialog ref="dialogRef" full-height full-width>
    <template v-if="loaded">
      <q-card class="column">
        <q-card-section class="row items-center q-py-sm">
          <div class="text-h6">Détail de la tâche #{{ task.id }}</div>
          <q-space/>
          <q-btn v-close-popup flat icon="close" round></q-btn>
        </q-card-section>

        <q-card-section class="col scroll">
          <div class="row q-col-gutter-sm full-height">
            <div class="col-xs-12 col-md-6 col-lg-7">
              <Form :initial-values="task" as="">
                <Field #default="{ errorMessage, value, field }" label="nom de la tâche" name="name" rules="required">
                  <label class="q-field__label" for="task_name">Nom de la tâche</label>
                  <q-input id="task_name" :error="!!errorMessage" :error-message="errorMessage" :model-value="value" aria-autocomplete="both" autocomplete class="q-mb-sm" dense outlined type="text" v-bind="field"/>
                </Field>

                <label class="q-field__label" for="task_description">Description</label>
                <q-input id="task_description" v-model="task.description" dense outlined type="textarea"></q-input>
              </Form>
            </div>

            <div class="col">
              <div class="column full-width full-height bg-grey-1 rounded-borders">
                <q-scroll-area class="col full-width">
                  <q-list>
                    <q-item v-for="comment in task.comments">
                      <q-item-section avatar>
                        <q-avatar color="primary" size="sm" text-color="white">{{ getInitials(comment.userUsername) }}</q-avatar>
                      </q-item-section>

                      <q-item-section>
                        <q-item-label class="cursor-pointer text-weight-bold text-indigo-4" lines="1" @click="showUserInfo(comment.userId)">{{ comment.userDataFullname }}<span v-if="authStore.user.id === comment.userId"> (vous)</span></q-item-label>
                        <q-item-label caption>{{ comment.content }}</q-item-label>
                      </q-item-section>
                    </q-item>
                  </q-list>
                </q-scroll-area>

                <q-space></q-space>

                <form id="form_comment" @submit.prevent="submitComment">
                  <q-input v-model="newComment" dense filled placeholder="Entrez votre commentaire" square>
                    <template #append>
                      <q-btn dense flat icon="send" round @click="submitComment"/>
                    </template>
                  </q-input>
                </form>
              </div>
            </div>
          </div>
        </q-card-section>

        <q-space></q-space>

        <q-card-actions align="right">
          <q-btn v-close-popup color="primary" unelevated>Fermer</q-btn>
        </q-card-actions>
      </q-card>
    </template>
  </q-dialog>
</template>

<script lang="ts" setup>
import { Dialog, QDialogOptions, useDialogPluginComponent } from "quasar";
import { onBeforeMount, ref } from "vue";
import { api } from "boot/axios";
import { TaskDto_Detail } from "app/.generated/rest";
import { getInitials } from "src/utils/string.utils";
import { useAuthStore } from "stores/auth";
import DialogUserInfo from "./DialogUserInfo.vue";

interface Props {
  taskId: number;
}

const props = defineProps<Props>();

defineEmits([
  ...useDialogPluginComponent.emits,
]);

const { dialogRef, onDialogHide, onDialogOK, onDialogCancel } = useDialogPluginComponent();

const authStore = useAuthStore();
const loaded = ref(false);
const newComment = ref<string>(null);
const task = ref<TaskDto_Detail>();

const fetchTask = async () => {
  const { success, result } = await api.getTaskDetails(props.taskId);

  if (success) {
    task.value = result;
  }
};

onBeforeMount(async () => {
  await fetchTask();
  loaded.value = true;
});

const submitComment = async () => {
  if (newComment != null && newComment.value.trim() != "") {
    const { success, result } = await api.postAddComment(task.value.id, { content: newComment.value });

    if (success) {
      task.value.comments.push(result);
      newComment.value = null;
    }
  }
};

const showUserInfo = (userId) => {
  const dialogOptions: QDialogOptions = {
    fullHeight: true,
    fullWidth: true,
    position: "right",
  };

  Dialog.create({
    component: DialogUserInfo,
    componentProps: {
      dialogOptions,
      userId,
    },
  });
};
</script>
