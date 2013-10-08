$(function() {

  var $form = $('form');
  var $text = $form.find('textarea');
  $form.submit(function() {
    $form.removeClass('has-success has-error');
    $.ajax({
      url: $form.attr('action'),
      data: $form.serialize(),
      type: 'post',
      success: function(code) {
        $form.addClass('has-success');
        $text.val(code);
      },
      error: function(a, b, c) {
        $form.addClass('has-error');
        $form.find('.error-message').text(a.responseText);
      }
    });
    return false;
  });
  $form.find('textarea, input, select').on('focus', function() {
    $form.removeClass('has-success has-error');
  });
});
