#include <stdio.h>
#include <stdlib.h>

#include <libavutil/opt.h>
#include <libavcodec/avcodec.h>
#include <libavformat/avformat.h>
#include <libswresample/swresample.h>

int decode_audio_file(const char* path, const int sample_rate, double** data, int* size, const int channel_map[])
{
    // get format from audio file
    AVFormatContext* format = avformat_alloc_context();
    if (avformat_open_input(&format, path, NULL, NULL) != 0)
    {
        fprintf(stderr, "Could not open file '%s'\n", path);
        return -1;
    }
    if (avformat_find_stream_info(format, NULL) < 0)
    {
        fprintf(stderr, "Could not retrieve stream info from file '%s'\n", path);
        return -1;
    }

    // Find the index of the first audio stream
    int stream_index = -1;
    for (uint16_t i = 0; i < (format->nb_streams); i++)
    {
        if (format->streams[i]->codecpar->codec_type == AVMEDIA_TYPE_AUDIO)
        {
            stream_index = i;
            break;
        }
    }
    if (stream_index == -1)
    {
        fprintf(stderr, "Could not retrieve audio stream from file '%s'\n", path);
        return -1;
    }
    AVStream* stream = format->streams[stream_index];

    // find & open codec
    AVCodecContext* codec = avcodec_alloc_context3(NULL);
    if (avcodec_parameters_to_context(codec, stream->codecpar) < 0)
    {
        fprintf(stderr, "Failed to copy value from parameter to context in file '%s'", path);
        return -1;
    }
    codec->pkt_timebase = stream->time_base;

    if (avcodec_open2(codec, avcodec_find_decoder(codec->codec_id), NULL) < 0)
    {
        fprintf(stderr, "Failed to open decoder for stream #%u in file '%s'\n", stream_index, path);
        return -1;
    }
    if ((channel_map[0] == 1 && stream->codecpar->channels <= 1) || (channel_map[0] == 0 && stream->codecpar->channels <= 0))
    {
        fprintf(stderr, "Failed to fined %d channel(-s) in file '%s'\n", channel_map[0] + 1, path);
        return -1;
    }

    // prepare resampler
    struct SwrContext* swr = swr_alloc();
    av_opt_set_int(swr, "in_channel_layout", stream->codecpar->channel_layout, 0);
    av_opt_set_int(swr, "out_channel_layout", AV_CH_LAYOUT_MONO, 0);
    av_opt_set_int(swr, "in_sample_rate", codec->sample_rate, 0);
    av_opt_set_int(swr, "out_sample_rate", sample_rate, 0);
    av_opt_set_sample_fmt(swr, "in_sample_fmt", codec->sample_fmt, 0);
    av_opt_set_sample_fmt(swr, "out_sample_fmt", AV_SAMPLE_FMT_DBL, 0);
    swr_set_channel_mapping(swr, channel_map);
    swr_init(swr);
    if (!swr_is_initialized(swr))
    {
        fprintf(stderr, "Resampler has not been properly initialized\n");
        return -1;
    }

    // prepare to read data
    // AVPacket packet;
    AVFrame* frame = av_frame_alloc();
    if (!frame)
    {
        fprintf(stderr, "Error allocating the frame\n");
        return -1;
    }

    // iterate through frames
    *data = NULL;
    *size = 0;
    double* buffer;

    AVPacket packet = { 0 };  // initialize packet
    while (av_read_frame(format, &packet) >= 0)
    {
        // send packet to decoder
        if (avcodec_send_packet(codec, &packet) < 0)
        {
            fprintf(stderr, "Error sending a packet for decoding\n");
            break;
        }
        // receive decoded frame
        while (avcodec_receive_frame(codec, frame) >= 0)
        {
            // resample frames
            av_samples_alloc((uint8_t**)&buffer, NULL, 1, frame->nb_samples, AV_SAMPLE_FMT_DBL, 0);
            int frame_count =
                    swr_convert(swr, (uint8_t**)&buffer, frame->nb_samples, (const uint8_t**)frame->data, frame->nb_samples);
            // append resampled frames to data
            *data = (double*)realloc(*data, (*size + frame->nb_samples) * sizeof(double));
            memcpy(*data + *size, buffer, frame_count * sizeof(double));
            *size += frame_count;
            av_freep(&buffer);
        }
        av_packet_unref(&packet);   // unreference packet
    }

    // clean up
    av_frame_free(&frame);
    swr_free(&swr);
    avcodec_close(codec);
    avformat_free_context(format);
    av_freep(&buffer);

    // success
    return 0;
}


int main(int argc, char const *argv[]) {

    // check parameters
    if (argc < 2) {
        fprintf(stderr, "Please provide the path to an audio file as first command-line argument.\n");
        return -1;
    }

    // decode data
    int sample_rate = 44100;
    double* data;
    int size;
    if (decode_audio_file(argv[1], sample_rate, &data, &size) != 0) {
        return -1;
    }

    // sum data
    double sum = 0.0;
    for (int i=0; i<size; ++i) {
        sum += data[i];
    }

    // display result and exit cleanly
    printf("sum is %f", sum);
    free(data);
    return 0;
}
