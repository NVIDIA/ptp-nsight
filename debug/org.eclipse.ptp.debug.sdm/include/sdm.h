/*
 * sdm.h
 *
 *  Created on: May 23, 2008
 *      Author: greg
 */

#ifndef SDM_H_
#define SDM_H_

#define SDM_AGGREGATE_TIMEOUT	0
#define SDM_AGGREGATE_HASH		1

struct sdm_idset;
struct sdm_message;
struct sdm_aggregate;

typedef int						sdm_id;
typedef struct sdm_idset *		sdm_idset;
typedef struct sdm_message *	sdm_message;
typedef struct sdm_aggregate *	sdm_aggregate;

extern sdm_id	SDM_MASTER;
/*
 * Startup/Initialization
 */
extern int sdm_init(int argc, char *argv[]);
extern int sdm_connect(const sdm_idset ids);
extern void sdm_finalize(void);
extern void sdm_progress(void);

/*
 * Communication
 */
extern int sdm_message_init(int argc, char *argv[]);
extern void sdm_message_finalize(void);
extern sdm_message sdm_message_new(char *buf, int len);
extern void sdm_message_free(sdm_message msg);
extern sdm_idset sdm_message_get_destination(const sdm_message msg);
extern sdm_aggregate sdm_message_get_aggregate(const sdm_message msg);
extern sdm_idset sdm_message_get_source(const sdm_message msg);
extern void sdm_message_set_source(const sdm_message msg, const sdm_idset src);
extern void sdm_message_get_payload(const sdm_message msg, char **buf, int *len);
extern int sdm_message_send(const sdm_message msg);
extern void sdm_message_set_send_callback(const sdm_message msg, void (*callback)(const sdm_message msg));
extern void sdm_message_set_recv_callback(void (*callback)(sdm_message msg));
extern int sdm_message_progress(void);

/*
 * Set operations
 */
extern sdm_idset sdm_set_new(void);
extern void sdm_set_free(sdm_idset set);
extern sdm_idset sdm_set_clear(sdm_idset set);
extern int sdm_set_size(const sdm_idset set);
extern sdm_idset sdm_set_add_element(const sdm_idset set, const sdm_id id);
extern sdm_idset sdm_set_remove_element(const sdm_idset set, const sdm_id id);
extern sdm_idset sdm_set_add_all(const sdm_idset set, const sdm_id id);
extern int sdm_set_is_subset(const sdm_idset set1, const sdm_idset set2);
extern int sdm_set_is_empty(const sdm_idset set);
extern int sdm_set_compare(const sdm_idset set1, const sdm_idset set2);
extern sdm_idset sdm_set_union(const sdm_idset set1, const sdm_idset set2);
extern sdm_idset sdm_set_intersect(const sdm_idset set1, const sdm_idset set2);
extern sdm_idset sdm_set_diff(const sdm_idset set1, const sdm_idset set2);
extern int sdm_set_contains(const sdm_idset set, const sdm_id id);
extern sdm_id sdm_set_max(const sdm_idset set);
extern sdm_id sdm_set_first(const sdm_idset set);
extern sdm_id sdm_set_next(const sdm_idset set);
extern int sdm_set_done(const sdm_idset set);
extern void sdm_set_serialize(const sdm_idset set, char *buf, char **end);
extern int sdm_set_serialized_length(const sdm_idset set);
extern void sdm_set_deserialize(sdm_idset set, char *str, char **end);
extern char *_set_to_str(const sdm_idset set);

/*
 * Routing
 */
extern int sdm_route_init(int argc, char *argv[]);
extern void sdm_route_finalize(void);
extern sdm_id sdm_route_get_parent(void);
extern sdm_id sdm_route_get_id(void);
extern void sdm_route_set_id(sdm_id id);
extern int sdm_route_get_size(void);
extern void sdm_route_set_size(int s);
extern sdm_idset sdm_route_get_route(const sdm_idset dest);
extern sdm_idset sdm_route_reachable(const sdm_idset dest);

/*
 * Aggregation
 */
extern int sdm_aggregate_init(int argc, char *argv[]);
extern void	sdm_aggregate_finalize(void);
extern sdm_aggregate sdm_aggregate_new(void);
extern void sdm_aggregate_free(sdm_aggregate a);
extern void sdm_aggregate_serialize(const sdm_aggregate a, char *buf, char **end);
extern int sdm_aggregate_serialized_length(const sdm_aggregate a);
extern void sdm_aggregate_deserialize(sdm_aggregate a, char *str, char **end);
extern void sdm_aggregate_set_completion_callback(void (*callback)(const sdm_message msg, void *data), void *data);
extern void sdm_aggregate_get_value(const sdm_aggregate a, int type, ...);
extern void sdm_aggregate_set_value(const sdm_aggregate a, int type, ...);
extern void sdm_aggregate_start(const sdm_message msg);
extern void sdm_aggregate_message(const sdm_message msg);
extern void sdm_aggregate_finish(const sdm_message msg);
extern void	sdm_aggregate_progress(void);
extern char * _aggregate_to_str(sdm_aggregate a);


/*
 * Payload
 */
extern void sdm_payload_set_callback(void (*callback)(const sdm_message msg, void *data), void *data);
extern void	sdm_payload_deliver(const sdm_message msg);

/*
 * I/O forwarding
 */
extern int sdm_iof_set_stdin_handler(int fd_in, int fd_out, int (*stdin_handler)(int fd_in, int fd_out));
extern int sdm_iof_set_stdout_handler(int fd_in, int fd_out, int (*stdout_handler)(int fd_in, int fd_out));
extern int sdm_iof_set_stderr_handler(int fd_in, int fd_out, int (*stderr_handler)(int fd_in, int fd_out));

#endif /* SDM_H_ */
