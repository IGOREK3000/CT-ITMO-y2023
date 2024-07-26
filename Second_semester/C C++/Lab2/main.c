#include <stdio.h>
#include <stdlib.h>
 
#include <libavutil/opt.h>
#include <libavcodec/avcodec.h>
#include <libavformat/avformat.h>
#include <libswresample/swresample.h>
 

#include <fftw3.h>

void cross_correlation(double* a, double* b, size_t new_size, double* result) {
    // Calculate the size for FFT (next power of 2 greater than na + nb - 1)

    // Allocate memory for FFT inputs and outputs
    fftw_complex *fft_a = (fftw_complex *)fftw_malloc(sizeof(fftw_complex) * new_size);
    fftw_complex *fft_b = (fftw_complex *)fftw_malloc(sizeof(fftw_complex) * new_size);
    fftw_complex *fft_result = (fftw_complex *)fftw_malloc(sizeof(fftw_complex) * new_size);

    // Create FFTW plans
    fftw_plan plan_fwd_a = fftw_plan_dft_r2c_1d(new_size, a, fft_a, FFTW_ESTIMATE);
    fftw_plan plan_fwd_b = fftw_plan_dft_r2c_1d(new_size, b, fft_b, FFTW_ESTIMATE);
    fftw_plan plan_bwd_result = fftw_plan_dft_c2r_1d(new_size, fft_result, result, FFTW_ESTIMATE);

    // Execute FFT for a and b
    fftw_execute(plan_fwd_a);
    fftw_execute(plan_fwd_b);

    // Element-wise multiplication in the frequency domain
    for (size_t i = 0; i < new_size; i++) {
        fft_result[i][0] = fft_a[i][0] * fft_b[i][0] + fft_a[i][1] * fft_b[i][1]; // Real part
        fft_result[i][1] = -fft_a[i][0] * fft_b[i][1] + fft_a[i][1] * fft_b[i][0]; // Imaginary part
    }

    // Execute inverse FFT to get cross-correlation
    fftw_execute(plan_bwd_result);


    // Clean up
    fftw_destroy_plan(plan_fwd_a);
    fftw_destroy_plan(plan_fwd_b);
    fftw_destroy_plan(plan_bwd_result);
    fftw_free(fft_a);
    fftw_free(fft_b);
    fftw_free(fft_result);
}

double* make_new_array(double* a, size_t size_a, size_t new_size) {
  double* new_arr = (double*)malloc(new_size * sizeof(double));
  for (size_t i = 0; i < new_size; ++i) {
    new_arr[i] = size_a > i? a[i] : 0.0;
  }
  return new_arr;
}



size_t find_shift(double* a, size_t size_a, double *b, size_t size_b) {
    if (a == NULL || b == NULL) {
        // Проверка на нулевой указатель
        return -1;
    }

    size_t new_size = size_a + size_b - 1;
    double* new_a = (double*)malloc(new_size * sizeof(double));
    double* new_b = (double*)malloc(new_size * sizeof(double));

    if (new_a == NULL || new_b == NULL) {
        // Проверка на успешное выделение памяти
        printf("Error allocating memory \n");
        free(new_a);
        free(new_b);
        return -1;
    }

    for (size_t i = 0; i < new_size; ++i) {
        new_a[i] = size_a > i? a[i] : 0.0;
        new_b[i] = size_b > i? b[i] : 0.0;
    }

    double* result = (double *)malloc(new_size * sizeof(double));
    if (result == NULL) {
        // Проверка на успешное выделение памяти
        printf("Error allocating memory\n");
        free(new_a);
        free(new_b);
        return -1;
    }

    cross_correlation(new_a, new_b, new_size, result);

    free(new_a);
    free(new_b);

    size_t max_ind = 0;
    double max_cor = -1;
    for (size_t i = 0; i < new_size; i++) {
        if (result[i] > max_cor) {
            max_cor = result[i];
            max_ind = i;
        }
    }
    free(result);
    if (max_ind > size_a) {
        return max_ind - new_size;
    } else {
        return max_ind;
    }
}
// Function to compute cross-correlation using FFT

 

int decode_audio_file(const char* path, int channel_flag, int* sample_rate, double** data, int* size)
{
  int channel_map[] = { channel_flag, -1 };

  AVFormatContext* format;
  AVStream* stream;
  AVCodecContext* codec;
  struct SwrContext* swr;
  AVPacket packet;
  AVFrame* frame;
  int stream_index = -1;

  format = avformat_alloc_context();
  if (avformat_open_input(&format, path, NULL, NULL) != 0)
    return -1;
  if (avformat_find_stream_info(format, NULL) < 0)
    return -1;

  for (size_t i = 0; i < format->nb_streams; i++)
  {
    if (format->streams[i]->codec->codec_type == AVMEDIA_TYPE_AUDIO)
    {
      stream_index = i;
      break;
    }
  }

  if (stream_index == -1)
    return -1;

  stream = format->streams[stream_index];
  codec = stream->codec;
  *sample_rate = codec->sample_rate;

  if (avcodec_open2(codec, avcodec_find_decoder(codec->codec_id), NULL) < 0)
    return -1;

  swr = swr_alloc();
  av_opt_set_sample_fmt(swr, "in_sample_fmt", codec->sample_fmt, 0);
  av_opt_set_sample_fmt(swr, "out_sample_fmt", AV_SAMPLE_FMT_DBL, 0);
  av_opt_set_int(swr, "in_sample_rate", codec->sample_rate, 0);
  av_opt_set_int(swr, "out_sample_rate", codec->sample_rate, 0);
  swr_set_channel_mapping(swr, channel_map);
  av_opt_set_int(swr, "in_channel_count", codec->channels, 0);
  av_opt_set_int(swr, "out_channel_count", 1, 0);
  av_opt_set_int(swr, "in_channel_layout", codec->channel_layout, 0);
  av_opt_set_int(swr, "out_channel_layout", AV_CH_LAYOUT_MONO, 0);
  swr_init(swr);

  if (channel_flag == 1 && codec->channels < 2)
    return -1;

  if (!swr_is_initialized(swr))
    return -1;

  frame = av_frame_alloc();
  if (!frame)
    return -1;

  int gotFrame;
  double* buffer;
  int size_double = sizeof(double);
  int frame_count;
  uint8_t** buffer_pointer;
  const uint8_t** frame_data_pointer;

  while (av_read_frame(format, &packet) >= 0)
  {
    if (avcodec_decode_audio4(codec, frame, &gotFrame, &packet) < 0)
      break;
    if (!gotFrame)
      continue;

    gotFrame = 0;
    buffer = NULL;
    frame_data_pointer = (const uint8_t**)frame->data;
    buffer_pointer = (uint8_t**)&buffer;

    av_samples_alloc(buffer_pointer, NULL, 1, frame->nb_samples, AV_SAMPLE_FMT_DBL, 0);

    frame_count = swr_convert(swr, buffer_pointer, frame->nb_samples, frame_data_pointer, frame->nb_samples);
    
    *data = realloc(*data, (*size + frame_count) * size_double);
    

    memcpy(*data + *size, buffer, frame_count * size_double);
    *size += frame_count;
  }

  av_frame_free(&frame);
  swr_free(&swr);
  avcodec_close(codec);
  avformat_free_context(format);

  return 0;
}

int main(int argc, char const *argv[]) {
    // check parameters
    if (argc < 2) {
        fprintf(stderr, "Please provide the path to an audio file as first command-line argument.\n");
        return -1;
    }

    
 
    // decode data
    int sample_rate_1;
    int sample_rate_2;
    double* data_1 = NULL;
    double* data_2 = NULL;
    int size_1 = 0;
    int size_2 = 0;
    int file1_num = 1;
    int file2_num = 2;
    int channel1_flag = 0;
    int channel2_flag = 0;
    if (argc == 2) {
      file2_num = 1;
      channel2_flag = 1;
    }
    
    if (decode_audio_file(argv[file1_num], channel1_flag, &sample_rate_1, &data_1, &size_1) != 0) {
        return -1;
    }
    if (decode_audio_file(argv[file2_num], channel2_flag, &sample_rate_2, &data_2, &size_2) != 0) {
        return -1;
    }

  
    size_t cor = find_shift(data_1, size_1, data_2, size_2);
  
    // display result and exit cleanly
    printf("delta: %i samples\nsample rate: %i Hz\ndelta time: %i ms\n",
      (int) cor,
      (int) sample_rate_1,
      (int) ((double)((int)cor) / (double)sample_rate_1 * 1000));
    free(data_1);
    free(data_2);
    return 0;
}