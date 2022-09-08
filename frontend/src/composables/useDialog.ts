function useDialog() {
  const { emit, proxy } = getCurrentInstance();

  const dialogRef = ref();

  const show = () => {
    dialogRef.value.show();
    emit("show");
  };

  const hide = () => {
    dialogRef.value.hide();
    emit("hide");
  };

  const close = () => {
    dialogRef.value.close();
    emit("close");
  };

  const onDialogOk = (payload?) => {
    emit("ok", payload);
  };

  const onDialogCancel = (payload?) => {
    emit("cancel", payload);
  };

  const onDialogHide = () => {
    emit("hide");
  };

  const onDialogClose = () => {
    emit("close");
  };

  Object.assign(proxy, { show, hide, close });

  return {
    dialogRef,
    onDialogHide,
    onDialogOk,
    onDialogCancel,
    onDialogClose,
  };
}

const emits = [ "ok", "cancel", "close", "hide" ];

useDialog.emits = emits;
useDialog.emitsObject = getEmitsObject(emits);

export default useDialog;
